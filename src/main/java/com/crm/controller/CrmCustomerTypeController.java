package com.crm.controller;

import com.crm.common.Result;
import com.crm.entity.CrmCustomerType;
import com.crm.service.CrmCustomerTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/crm/customerType")
public class CrmCustomerTypeController {

    @Resource
    private CrmCustomerTypeService customerTypeService;

    @GetMapping("/list")
    public Result<List<CrmCustomerType>> list() {
        return Result.success(customerTypeService.listByMerchant());
    }
}
