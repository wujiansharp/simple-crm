package com.crm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.entity.CrmOrder;
import com.crm.entity.OrderSaveDTO;
import com.crm.service.CrmOrderService;
import com.crm.util.LoginUserContext;
import com.crm.vo.CrmOrderVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/crm/order")
public class CrmOrderController {

    @Resource
    private CrmOrderService orderService;

    /**
     * 根据商户ID查询订单列表
     */
    @GetMapping("/listByMerchant")
    public Result<List<CrmOrder>> listByMerchant() {
        List<CrmOrder> list = orderService.listByMerchant();
        return Result.success(list);
    }


    @PostMapping("/save")
    public Result<String> save(@RequestBody OrderSaveDTO dto) {

        orderService.addOrder(dto);
        return Result.success("下单成功");
    }
    /**
     * 分页查询订单
     */
    @GetMapping("/page")
    public Result page(
            @RequestParam(required = false) String customerName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<CrmOrderVO> page = orderService.getOrderPage( customerName, pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        CrmOrderVO vo=orderService.getOrderDetail(id);
        return Result.success(vo);
    }
    // 删除订单
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        CrmOrder order = new CrmOrder();
        order.setId(id);
        order.setDelFlag(0);
        orderService.updateById(order);
        return Result.success(true);
    }
}
