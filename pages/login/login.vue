<template>
  <div class="login-container">
    <div class="login-logo-box">
      <img src="/src/assets/logo.png" alt="系统LOGO" class="logo-img" />
    </div>
    <div class="login-box">
      <div class="title">小微企业CRM</div>
      <input v-model="username" placeholder="请输入账号" class="inp" />
      <input v-model="password" placeholder="请输入密码" type="password" class="inp" />
      <button @click="handleLogin" class="login-btn">登录</button>
    </div>
    <div class="copyright">
      ©2026 助商舟科技 专属定制CRM系统 版权所有  独立部署·数据私有
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { onLogin } from '../../utils/api.js'

const router = useRouter()

const username = ref('')
const password = ref('')

const handleLogin = async () => {
  if (!username.value || !password.value) {
    alert('请输入账号密码')
    return
  }
  try {
    const res = await onLogin(username.value, password.value)
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('merchantId', res.data.merchantId)
      localStorage.setItem('realName', res.data.realName)
      localStorage.setItem('roleName', res.data.roleName)
      localStorage.setItem('roleType', res.data.roleType)
      alert('登录成功')
      router.push('/')
    } else {
      alert(res.msg || '登录失败')
    }
  } catch (err) {
    alert('登录异常')
    console.error(err)
  }
}
</script>

<style scoped>
/* 基础全屏布局 */
.login-container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  position: relative;
  padding: 20px;
  box-sizing: border-box;
}

/* LOGO */
.login-logo-box {
  margin-bottom: 20px;
}
.logo-img {
  width: 90px;
  max-width: 30vw;
  object-fit: contain;
}

/* 登录框 - 核心自适应 */
.login-box {
  width: 100%;
  max-width: 380px;
  padding: 30px 24px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.06);
  box-sizing: border-box;
}

.title {
  font-size: clamp(20px, 5vw, 24px);
  font-weight: bold;
  text-align: center;
  margin-bottom: 26px;
  color: #333;
}

.inp {
  border: 1px solid #eee;
  border-radius: 10px;
  padding: 14px;
  margin-bottom: 18px;
  width: 100%;
  box-sizing: border-box;
  font-size: 15px;
  outline: none;
  transition: border-color 0.2s;
}
.inp:focus {
  border-color: #2d8cf0;
}

.login-btn {
  background: #2d8cf0;
  color: #fff;
  border-radius: 10px;
  padding: 14px;
  width: 100%;
  border: none;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s;
}
.login-btn:hover {
  background: #1a6bbf;
}

/* 版权 */
.copyright {
  position: absolute;
  bottom: 12px;
  left: 0;
  width: 100%;
  text-align: center;
  color: #999;
  font-size: 11px;
  padding: 0 10px;
  box-sizing: border-box;
}
</style>