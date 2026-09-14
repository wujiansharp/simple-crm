package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.CrmFollowRecord;
import com.crm.mapper.CrmFollowRecordMapper;
import com.crm.service.CrmFollowRecordService;
import com.crm.util.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrmFollowRecordServiceImpl extends ServiceImpl<CrmFollowRecordMapper, CrmFollowRecord> implements CrmFollowRecordService {

    @Override
    public Page<CrmFollowRecord> getRecordPage(Integer pageNum, Integer pageSize, Long customerId) {
        LambdaQueryWrapper<CrmFollowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmFollowRecord::getCustomerId, customerId);
        wrapper.orderByDesc(CrmFollowRecord::getCreateTime);
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRecord(CrmFollowRecord record) {
        record.setMerchantId(LoginUserContext.getMerchantId());
        record.setUserId(LoginUserContext.getUserId());
        return this.save(record);
    }

    @Override
    public List<CrmFollowRecord> listByMerchant() {
        LambdaQueryWrapper<CrmFollowRecord> wrapper = new LambdaQueryWrapper<>();
         wrapper.eq(CrmFollowRecord::getMerchantId, LoginUserContext.getMerchantId())
                .eq(!LoginUserContext.isAdmin(), CrmFollowRecord::getUserId, LoginUserContext.getUserId())
                .orderByDesc(CrmFollowRecord::getFollowTime);

        return this.list(wrapper);
    }

    @Override
    public int countWaitFollow() {
        long count = this.lambdaQuery()
                .eq(CrmFollowRecord::getMerchantId, LoginUserContext.getMerchantId())
                .eq(!LoginUserContext.isAdmin(), CrmFollowRecord::getUserId, LoginUserContext.getUserId())
                .count();
        return (int) count;
    }

    @Override
    public List<CrmFollowRecord> listWaitFollow() {
        Long userId = LoginUserContext.getUserId();
        LambdaQueryWrapper<CrmFollowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmFollowRecord::getUserId, userId)
                .isNotNull(CrmFollowRecord::getNextFollowTime)
                .ge(CrmFollowRecord::getNextFollowTime, LocalDateTime.now())
                .orderByAsc(CrmFollowRecord::getNextFollowTime);
        return list(wrapper);
    }
}
