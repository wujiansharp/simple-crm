package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.entity.CrmOrder;
import com.crm.entity.OrderSaveDTO;
import com.crm.vo.CrmOrderVO;

import java.util.List;

public interface CrmOrderService extends IService<CrmOrder> {
    public Page<CrmOrderVO> getOrderPage( String customerName, Integer pageNum, Integer pageSize);
    boolean addOrder(OrderSaveDTO dto);

    /**
     * 根据商户ID查询订单列表
     */
    List<CrmOrder> listByMerchant();
    int countMonthOrder();
    CrmOrderVO getOrderDetail(Long id);
}
