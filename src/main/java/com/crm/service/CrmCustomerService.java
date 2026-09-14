package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.entity.CrmCustomer;

import java.util.List;

public interface CrmCustomerService extends IService<CrmCustomer> {
    /**
     * 分页查询客户列表
     */
    Page<CrmCustomer> getCustomerPage(Integer pageNum, Integer pageSize, CrmCustomer customer);

    /**
     * 根据ID查询客户详情（带关联信息）
     */
    CrmCustomer getCustomerById(Long id);

    /**
     * 新增客户
     */
    boolean addCustomer(CrmCustomer customer);

    /**
     * 修改客户
     */
    boolean updateCustomer(CrmCustomer customer);

    /**
     * 删除客户
     */
    boolean deleteCustomer(Long id);

    List<CrmCustomer> list(String keyWord);
    int countCustomer();

}