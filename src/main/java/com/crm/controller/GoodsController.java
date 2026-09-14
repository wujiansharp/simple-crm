package com.crm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crm.common.Result;
import com.crm.entity.CrmGoods;
import com.crm.service.CrmGoodsService;
import com.crm.util.LoginUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crm/goods")
public class GoodsController {

    @Autowired
    private CrmGoodsService goodsService;

    @GetMapping("/list")
    public Result list() {
        Long merchantId = LoginUserContext.getMerchantId();
        return Result.success(goodsService.list(new LambdaQueryWrapper<CrmGoods>()
                .eq(CrmGoods::getMerchantId, merchantId)
                .eq(CrmGoods::getDelFlag,1)));
    }

    @PostMapping("/save")
    public Result save(@RequestBody CrmGoods goods) {
        goods.setMerchantId(LoginUserContext.getMerchantId());
        goodsService.saveOrUpdate(goods);
        return Result.success(true);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        CrmGoods goods = new CrmGoods();
        goods.setId(id);
        goods.setDelFlag(0);
        goodsService.updateById(goods);
        return Result.success(true);
    }
}
