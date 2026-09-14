<template>
  <view class="mine">
    <view class="user-info">
      <text class="name">{{userInfo.realName}}</text>
      <text class="role">{{userInfo.roleType===1?'商户管理员':'员工'}}</text>
    </view>
    <view class="menu">
      <view class="menu-item" @click="changePwd">修改密码</view>
      <view class="menu-item" @click="logout">退出登录</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo:{}
    }
  },
  onShow() {
    this.getUser()
  },
  methods: {
    async getUser(){
      let res = await this.$api.get("/sys/user/info")
      this.userInfo = res.data
    },
    changePwd(){
      uni.navigateTo({url:"/pages/login/login"})
    },
    logout(){
      uni.clearStorageSync()
      uni.reLaunch({url:"/pages/login/login"})
    }
  }
}
</script>