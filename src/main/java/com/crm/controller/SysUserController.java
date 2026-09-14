package com.crm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crm.common.Result;
import com.crm.entity.SysUser;
import com.crm.service.SysUserService;
import com.crm.util.JwtUtil;
import com.crm.util.LoginUserContext;
import com.crm.util.Md5Util;
import com.crm.util.TenantContext;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody SysUser user) {
        List<SysUser> userList = sysUserService.list(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, user.getUsername()).eq(SysUser::getStatus,1));
        if (userList.size() > 1) {
            // 处理重复数据，比如记录日志、删除多余记录
            return Result.fail("账号异常！");
        }
        if (userList == null ||userList.size()<1) {
            return Result.fail("账号或密码错误");
        }
        SysUser sysUser=userList.get(0);
        String encodeInputPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        if (!sysUser.getPassword().equals(encodeInputPwd)) {
            return Result.fail("账号或密码错误");
        }

        String token = JwtUtil.generateToken(sysUser.getId(), sysUser.getMerchantId(),sysUser.getRoleType());
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("token", token);
        resMap.put("merchantId", sysUser.getMerchantId());
        resMap.put("realName", sysUser.getRealName());
        resMap.put("roleType", sysUser.getRoleType());
        resMap.put("roleName", sysUser.getRoleType() == 1 ? "店长" : "店员");
        return Result.success(resMap);
    }

    @GetMapping("/info")
    public Result<SysUser> info() {
        Long userId = LoginUserContext.getUserId();
        SysUser user = sysUserService.getById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 修改密码（需要原密码）
     */
    @PostMapping("/updatePwd")
    public Result<Boolean> updatePwd(@RequestBody SysUser user) {
        System.out.println("meicuda!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return Result.success(sysUserService.updatePwd(user));
    }
    /**
     * 分页查询用户
     */
    @GetMapping("/employee/page")
    public Result<Page<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysUser sysUser) {
        Page<SysUser> page = sysUserService.getUserPage(pageNum, pageSize, sysUser);
        return Result.success(page);
    }
    /**
     * 根据ID查询
     */
    @GetMapping("/get/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return Result.success(sysUserService.getById(id));
    }
    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody SysUser sysUser) {
        return Result.success(sysUserService.save(sysUser));
    }
    /**
     * 修改用户
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody SysUser sysUser) {
        return Result.success(sysUserService.updateById(sysUser));
    }
    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(sysUserService.removeById(id));
    }
    /**
     * 管理员重置密码（无需原密码）
     */
    @PostMapping("/resetPwd")
    public Result<Boolean> resetPwd(
            @RequestParam Long userId) {
        return Result.success(sysUserService.resetPwd(userId));
    }
    @GetMapping("/employee/list")
    public Result listEmployee( @RequestParam(required = false) String userName,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        // 查询 角色=客户经理 的用户 == 雇员列表
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getRoleType, 2); // 假设3=客户经理角色ID
        wrapper.eq(SysUser::getMerchantId, LoginUserContext.getMerchantId());
        List<SysUser> list = sysUserService.list(wrapper);
        return Result.success(list);
    }
    /**
     * 删除用户
     */
    @PostMapping("/employee/delete/{id}")
    public Result<Boolean> deleteEmployee(@PathVariable Long id) {
        if(LoginUserContext.isAdmin()) {
            return Result.success(sysUserService.removeById(id));
        }else{
            return Result.fail("店员没有该权限！");
        }
    }
    @PostMapping("/employee/save")
    public Result<String> save(@RequestBody SysUser user) {
        sysUserService.addOrUpdateEmployee(user);
        return Result.success("新增成功");
    }

}