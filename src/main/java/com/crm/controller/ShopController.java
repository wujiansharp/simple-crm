package com.crm.controller;

import com.crm.common.Result;
import com.crm.entity.ShopMerchant;
import com.crm.service.ShopMerchantService;
import com.crm.util.LoginUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crm/shop")
public class ShopController {

    @Autowired
    private ShopMerchantService shopService;

    // 获取我的商铺
    @GetMapping("/my")
    public Result myShop() {
        return Result.success(shopService.getShopList().get(0));
    }

    // 新增/修改商铺
    @PostMapping("/save")
    public Result save(@RequestBody ShopMerchant shop) {
        Long userId = LoginUserContext.getUserId();
        shop.setUserId(userId); // 自动设置 userId
        shopService.saveOrUpdate(shop);
        return Result.success(shop);
    }
}
