package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.entity.ShopMerchant;
import java.util.List;

public interface ShopMerchantService extends IService<ShopMerchant> {
    List<ShopMerchant> getShopList();
}