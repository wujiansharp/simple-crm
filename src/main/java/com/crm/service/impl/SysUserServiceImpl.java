package com.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crm.entity.SysUser;
import com.crm.mapper.SysUserMapper;
import com.crm.service.SysUserService;
import com.crm.util.LoginUserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        // MD5加密（实际项目可替换为安全加密方式）
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        wrapper.eq(SysUser::getPassword, md5Pwd);
        return this.getOne(wrapper);
    }

    @Override
    public Page<SysUser> getUserPage(Integer pageNum, Integer pageSize, SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(user.getRealName()), SysUser::getRealName, user.getRealName());
        wrapper.eq(user.getStatus() != null, SysUser::getStatus, user.getStatus());
        wrapper.eq(SysUser::getMerchantId,LoginUserContext.getMerchantId());
        wrapper.eq(SysUser::getRoleType,2);
        wrapper.orderByDesc(SysUser::getCreateTime);
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePwd(SysUser user) {
        SysUser userOld = getById(user.getId());
        if (userOld == null) {
            throw new RuntimeException("用户不存在");
        }
        String newMd5 = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(newMd5);
        System.out.println(newMd5);
        System.out.println(user.getPassword());
        return updateById(user);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPwd(Long userId) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        return updateById(user);
    }

    @Override
    public boolean addOrUpdateEmployee(SysUser user) {
        user.setMerchantId( LoginUserContext.getMerchantId());
        user.setRoleType(2);
        user.setUsername(user.getPhone());
        String newMd5 = DigestUtils.md5DigestAsHex("123456".getBytes());
        user.setPassword(newMd5);
        saveOrUpdate(user);
        return true;
    }
}