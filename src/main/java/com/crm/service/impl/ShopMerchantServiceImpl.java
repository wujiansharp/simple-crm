package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmCustomer;
import com.crm.entity.CrmOrder;
import com.crm.entity.ShopMerchant;
import com.crm.mapper.ShopMerchantMapper;
import com.crm.service.ShopMerchantService;
import com.crm.util.LoginUserContext;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopMerchantServiceImpl extends ServiceImpl<ShopMerchantMapper, ShopMerchant> implements ShopMerchantService {

    @Override
    public List<ShopMerchant> getShopList() {
        LambdaQueryWrapper<ShopMerchant> wrapper = new LambdaQueryWrapper<>();
      //如果是店长商铺的用户id就是登陆用户，如果是店员用户的商铺id
        if(LoginUserContext.isAdmin()){
          wrapper.eq( ShopMerchant::getUserId, LoginUserContext.getUserId());
      }else {
          wrapper.eq(ShopMerchant::getId, LoginUserContext.getMerchantId());
      }
        wrapper.orderByAsc(ShopMerchant::getId);
        return this.list(wrapper);
    }
}