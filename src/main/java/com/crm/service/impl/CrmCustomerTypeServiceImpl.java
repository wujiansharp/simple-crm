package com.crm.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmCustomerType;
import com.crm.mapper.CrmCustomerTypeMapper;
import com.crm.service.CrmCustomerTypeService;
import com.crm.util.LoginUserContext;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrmCustomerTypeServiceImpl extends ServiceImpl<CrmCustomerTypeMapper, CrmCustomerType> implements CrmCustomerTypeService {

    @Override
    public List<CrmCustomerType> getTypeList() {
        LambdaQueryWrapper<CrmCustomerType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(CrmCustomerType::getId);
        return this.list(wrapper);
    }

    @Override
    public List<CrmCustomerType> listByMerchant() {
        return this.lambdaQuery()
                .eq(CrmCustomerType::getMerchantId,  LoginUserContext.getMerchantId())
                .list();
    }
}