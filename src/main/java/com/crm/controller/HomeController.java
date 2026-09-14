package com.crm.controller;

import com.crm.common.Result;
import com.crm.service.CrmCustomerService;
import com.crm.service.CrmFollowRecordService;
import com.crm.service.CrmOrderService;
import com.crm.util.LoginUserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmFollowRecordService followService;
    @Resource
    private CrmOrderService orderService;

    /**
     * 首页数据汇总
     */
    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        // 自动从登录上下文获取，前端不用传！
        Long merchantId = LoginUserContext.getMerchantId();

        Map<String, Object> map = new HashMap<>();
//        map.put("customerList", customerService.list(""));
//        map.put("followRecordList", followService.listByMerchant());
        map.put("customerNum", customerService.countCustomer());
        map.put("waitFollowNum", followService.countWaitFollow());
        map.put("monthOrderNum", orderService.countMonthOrder());

        return map;
    }
}