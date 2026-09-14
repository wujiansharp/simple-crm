package com.crm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmCustomer;
import com.crm.mapper.CrmCustomerMapper;
import com.crm.service.CrmCustomerService;
import com.crm.util.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CrmCustomerServiceImpl extends ServiceImpl<CrmCustomerMapper, CrmCustomer> implements CrmCustomerService {

    @Override
    public Page<CrmCustomer> getCustomerPage(Integer pageNum, Integer pageSize, CrmCustomer customer) {
        LambdaQueryWrapper<CrmCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(customer.getCustomerName()), CrmCustomer::getCustomerName, customer.getCustomerName());
        wrapper.eq(customer.getTypeId() != null, CrmCustomer::getTypeId, customer.getTypeId());
        wrapper.eq(customer.getMerchantId() != null, CrmCustomer::getMerchantId, customer.getMerchantId());
        wrapper.eq(customer.getUserId() != null, CrmCustomer::getUserId, customer.getUserId());
        wrapper.orderByDesc(CrmCustomer::getCreateTime);
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public CrmCustomer getCustomerById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomer(CrmCustomer customer) {
        customer.setUserId(LoginUserContext.getUserId());
        customer.setMerchantId(LoginUserContext.getMerchantId());
        return this.saveOrUpdate(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCustomer(CrmCustomer customer) {
        return this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomer(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<CrmCustomer> list(String keyWord) {
        return this.lambdaQuery()
                .eq(CrmCustomer::getMerchantId, LoginUserContext.getMerchantId())
                // 非管理员 才加用户隔离
                .eq(!LoginUserContext.isAdmin(), CrmCustomer::getUserId, LoginUserContext.getUserId())
                .like(org.springframework.util.StringUtils.hasText(keyWord), CrmCustomer::getCustomerName, keyWord)
                .list();
    }

    @Override
    public int countCustomer() {
        long count = this.lambdaQuery()
                .eq(CrmCustomer::getMerchantId,  LoginUserContext.getMerchantId())
                .eq(!LoginUserContext.isAdmin(), CrmCustomer::getUserId, LoginUserContext.getUserId())
                .count();
        return (int) count;
    }
}