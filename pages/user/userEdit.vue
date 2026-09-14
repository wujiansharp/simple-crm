<template>
  <div class="page">
    <div class="title">我的信息</div>

    <div class="form-item">
      <text>姓名</text>
      <input v-model="form.realName" placeholder="请输入姓名" />
    </div>

    <div class="form-item">
      <text>手机号/账号</text>
      <input v-model="form.phone" disabled placeholder="不可修改" />
    </div>

    <div class="form-item">
      <text>角色</text>
      <input :value="form.roleType === 1 ? '店长' : '店员'" disabled placeholder="角色" />
    </div>

    <div class="title">修改密码</div>

    <div class="form-item">
      <text>新密码</text>
      <input v-model="form.password" placeholder="留空则不修改" type="password" />
    </div>

    <div class="form-item">
      <text>确认新密码</text>
      <input v-model="confirmPwd" placeholder="请再次输入密码" type="password" />
    </div>

    <button class="save-btn" @click="save">保存</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { updateMyInfo, getMyInfo } from '../../utils/api.js'

const router = useRouter()

const form = ref({
  id: '',
  realName: '',
  phone: '',
  roleType: 2,
  password: ''
})

const confirmPwd = ref('')

onMounted(async () => {
  const res = await getMyInfo()
  if (res.code === 200) {
    form.value = res.data
  }
})

const save = async () => {
  if (!form.value.realName) {
    alert('请输入姓名')
    return
  }

  if (form.value.password && form.value.password !== confirmPwd.value) {
    alert('两次密码不一致')
    return
  }

  const res = await updateMyInfo(form.value)
  if (res.code === 200) {
    alert('保存成功')
    router.back()
  } else {
    alert(res.msg || '保存失败')
  }
}
</script>

<style scoped>
.page {
  padding: 20rpx;
}
.title {
  font-size: 30rpx;
  font-weight: bold;
  margin: 30rpx 0 20rpx;
}
.form-item {
  margin-bottom: 25rpx;
}
.form-item text {
  display: block;
  margin-bottom: 10rpx;
  font-size: 28rpx;
}
input {
  width: 100%;
  padding: 20rpx;
  border: 1px solid #eee;
  border-radius: 10rpx;
  box-sizing: border-box;
  font-size: 28rpx;
}
.save-btn {
  width: 100%;
  background: #2d8cf0;
  color: #fff;
  padding: 24rpx;
  border: none;
  border-radius: 10rpx;
  margin-top: 30rpx;
  font-size: 30rpx;
}
</style>