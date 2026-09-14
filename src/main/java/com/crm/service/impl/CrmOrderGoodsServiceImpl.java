package com.crm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmOrderGoods;
import com.crm.mapper.CrmOrderGoodsMapper;
import com.crm.service.CrmOrderGoodsService;
import org.springframework.stereotype.Service;

@Service
public class CrmOrderGoodsServiceImpl extends ServiceImpl<CrmOrderGoodsMapper, CrmOrderGoods> implements CrmOrderGoodsService {
}
