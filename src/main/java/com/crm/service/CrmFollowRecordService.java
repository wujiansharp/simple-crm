package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.entity.CrmFollowRecord;

import java.util.List;

public interface CrmFollowRecordService extends IService<CrmFollowRecord> {
    Page<CrmFollowRecord> getRecordPage(Integer pageNum, Integer pageSize, Long customerId);
    boolean addRecord(CrmFollowRecord record);
    List<CrmFollowRecord> listByMerchant();
    int countWaitFollow();
    // 待回访记录
    List<CrmFollowRecord> listWaitFollow();

}
