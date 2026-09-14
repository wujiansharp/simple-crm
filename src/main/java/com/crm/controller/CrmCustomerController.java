package com.crm.controller;

import com.crm.common.Result;
import com.crm.entity.CrmCustomer;
import com.crm.service.CrmCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crm/customer")
public class CrmCustomerController {
    @Autowired
    private CrmCustomerService customerService;

    @GetMapping("/list")
    public Result<List<CrmCustomer>> list(String keyWord){
        return Result.success(customerService.list(keyWord));
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody CrmCustomer customer){
        customerService.addCustomer(customer);
        return Result.success("保存成功");
    }
    @GetMapping("/info/{id}")
    public Result<CrmCustomer> info(@PathVariable Long id){
        CrmCustomer customer=customerService.getCustomerById(id);
        return Result.success(customer);
    }
}