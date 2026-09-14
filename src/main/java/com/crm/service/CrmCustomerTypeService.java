package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.CrmCustomerType;
import java.util.List;

public interface CrmCustomerTypeService extends IService<CrmCustomerType> {
    List<CrmCustomerType> getTypeList();
    List<CrmCustomerType> listByMerchant();

}