package com.crm.controller;

import com.crm.common.Result;
import com.crm.entity.CrmFollowRecord;
import com.crm.service.CrmFollowRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/crm/follow")
public class CrmFollowController {

    @Resource
    private CrmFollowRecordService followRecordService;

    @GetMapping("/list")
    public Result<List<CrmFollowRecord>> list() {
        return Result.success(followRecordService.listByMerchant());
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody CrmFollowRecord record) {
        followRecordService.addRecord(record);
        return Result.success("添加跟进成功");
    }

    // 待回访列表
    @GetMapping("/wait")
    public Result waitFollow() {
        return Result.success(followRecordService.listWaitFollow());
    }
    // 删除
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        followRecordService.removeById(id);
        return Result.success(true);
    }
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id) {
        return Result.success(followRecordService.getById(id));
    }
}
