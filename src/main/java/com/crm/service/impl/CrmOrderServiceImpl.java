package com.crm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.*;
import com.crm.mapper.CrmOrderMapper;
import com.crm.service.CrmCustomerService;
import com.crm.service.CrmGoodsService;
import com.crm.service.CrmOrderGoodsService;
import com.crm.service.CrmOrderService;
import com.crm.util.LoginUserContext;
import com.crm.vo.CrmOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrmOrderServiceImpl extends ServiceImpl<CrmOrderMapper, CrmOrder> implements CrmOrderService {
    @Autowired
    private CrmCustomerService customerService;
    @Autowired
    private CrmOrderGoodsService orderGoodsService;
    @Autowired
    private CrmGoodsService crmGoodsService;
    @Override
    public Page<CrmOrderVO> getOrderPage(String customerName, Integer pageNum, Integer pageSize) {

        // 1. 构建分页对象（这里必须用 Page，不能用 IPage）
        Page<CrmOrder> page = new Page<>(pageNum, pageSize);

        // 2. 查询条件
        var wrapper = Wrappers.lambdaQuery(CrmOrder.class)
                .eq(CrmOrder::getUserId, LoginUserContext.getUserId())
                .eq(CrmOrder::getMerchantId, LoginUserContext.getMerchantId())
                .eq(!LoginUserContext.isAdmin(), CrmOrder::getUserId, LoginUserContext.getUserId())
                .eq(CrmOrder::getDelFlag, 1);

        // 3. 分页查询
        IPage<CrmOrder> orderPage = this.page(page, wrapper);

        // 4. 转 VO，并设置客户名称
        List<CrmOrderVO> voList = orderPage.getRecords().stream().map(order -> {
            CrmOrderVO vo = new CrmOrderVO();
            BeanUtils.copyProperties(order, vo);
            List<CrmOrderGoods> goodsList = orderGoodsService.list(
                    Wrappers.lambdaQuery(CrmOrderGoods.class)
                            .eq(CrmOrderGoods::getOrderId, order.getId())
            );
            vo.setGoodsList(goodsList);
            // 填充客户名称
            if (order.getCustomerId() != null) {
                CrmCustomer customer = customerService.getById(order.getCustomerId());
                if (customer != null) {
                    vo.setCustomerName(customer.getCustomerName());
                }
            }
            return vo;
        }).collect(Collectors.toList());

        // 5. 组装返回分页
        Page<CrmOrderVO> resultPage = new Page<>(pageNum, pageSize);
        resultPage.setRecords(voList);
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setPages(orderPage.getPages());

        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrder(OrderSaveDTO dto) {
        CrmOrder order = dto.getOrder();
        order.setMerchantId(LoginUserContext.getMerchantId());
        order.setUserId(LoginUserContext.getUserId());
        if (order.getOrderNo() == null || order.getOrderNo().trim().isEmpty()) {
            // 获取当前时间：yyyyMMddHHmmss （年月日时分秒）
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String timeStr = now.format(formatter);

            // 生成4位随机数
            int random = (int) ((Math.random() * 9000) + 1000);

            // 拼接订单号
            String orderNo = "DD" + timeStr + random;
            order.setOrderNo(orderNo);
        }
         this.saveOrUpdate(order);
// 先删旧明细
        orderGoodsService.remove(new LambdaQueryWrapper<CrmOrderGoods>()
                .eq(CrmOrderGoods::getOrderId, order.getId()));
        for (CrmOrderGoods g : dto.getGoodsList()) {
            g.setOrderId(order.getId());
            g.setMerchantId(order.getMerchantId());

            // ========== 关键：自动同步到商品库 ==========
            if (g.getGoodsId() == null || g.getGoodsId() == 0) {
                // 1. 查询当前商户下是否已有同名商品
                LambdaQueryWrapper<CrmGoods> goodsWrapper = new LambdaQueryWrapper<>();
                goodsWrapper.eq(CrmGoods::getMerchantId, order.getMerchantId())
                        .eq(CrmGoods::getGoodsName, g.getGoodsName())
                        .eq(CrmGoods::getDelFlag, 1);
                CrmGoods existGoods = crmGoodsService.getOne(goodsWrapper);

                if (existGoods != null) {
                    // 已有商品：绑定id
                    g.setGoodsId(existGoods.getId());
                    g.setPrice(existGoods.getPrice());
                } else {
                    // 无商品：自动新建存入商品库
                    CrmGoods newGoods = new CrmGoods();
                    newGoods.setMerchantId(order.getMerchantId());
                    newGoods.setGoodsName(g.getGoodsName());
                    newGoods.setPrice(g.getPrice());
                    newGoods.setDelFlag(1);
                    crmGoodsService.save(newGoods);
                    // 把新生成的商品id回填到订单明细
                    g.setGoodsId(newGoods.getId());
                }
            }
            // 保存订单明细
            orderGoodsService.save(g);
        }
        return true;
    }
    @Override
    public List<CrmOrder> listByMerchant() {
        LambdaQueryWrapper<CrmOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmOrder::getMerchantId, LoginUserContext.getMerchantId());
        wrapper.eq(CrmOrder::getDelFlag, 1);
        wrapper.orderByDesc(CrmOrder::getCreateTime);
        return list(wrapper);
    }

    @Override
    public int countMonthOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = now.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthEnd = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        long count = this.lambdaQuery()
                .eq(CrmOrder::getMerchantId, LoginUserContext.getMerchantId())
                .eq(CrmOrder::getDelFlag, 1)
                .between(CrmOrder::getCreateTime, monthStart, monthEnd)
                .eq(!LoginUserContext.isAdmin(), CrmOrder::getUserId, LoginUserContext.getUserId())
                .count();
        return (int) count;
    }

    @Override
    public CrmOrderVO getOrderDetail(Long id) {
        // 1. 查询订单主表
        CrmOrder order = this.getById(id);
        if (order == null) {
            return new CrmOrderVO();
        }

        // 2. 查询订单商品列表
        List<CrmOrderGoods> goodsList = orderGoodsService.list(
                Wrappers.lambdaQuery(CrmOrderGoods.class)
                        .eq(CrmOrderGoods::getOrderId, id)
        );

        // 3. 组装返回 VO
        CrmOrderVO vo = new CrmOrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setGoodsList(goodsList);

        return vo;
    }

}
