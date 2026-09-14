package com.crm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser login(String username, String password);
    Page<SysUser> getUserPage(Integer pageNum, Integer pageSize, SysUser user);
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPwd 原密码明文
     * @param newPwd 新密码明文
     * @return 是否成功
     */
    boolean updatePwd(SysUser user);
    boolean resetPwd(Long userId);
    boolean addOrUpdateEmployee(SysUser user);
}