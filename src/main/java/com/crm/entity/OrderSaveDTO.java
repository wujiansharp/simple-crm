package com.crm.entity;

import java.util.List;

public class OrderSaveDTO {
    private CrmOrder order;
    private List<CrmOrderGoods> goodsList;

    public CrmOrder getOrder() {
        return order;
    }

    public void setOrder(CrmOrder order) {
        this.order = order;
    }

    public List<CrmOrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CrmOrderGoods> goodsList) {
        this.goodsList = goodsList;
    }
}
