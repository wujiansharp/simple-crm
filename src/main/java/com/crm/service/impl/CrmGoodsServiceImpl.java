package com.crm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmGoods;
import com.crm.mapper.CrmGoodsMapper;
import com.crm.service.CrmGoodsService;
import org.springframework.stereotype.Service;

@Service
public class CrmGoodsServiceImpl extends ServiceImpl<CrmGoodsMapper, CrmGoods> implements CrmGoodsService {
}
