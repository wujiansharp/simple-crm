<template>
  <div class="page">
    <!-- 搜索 -->
    <div class="search-box">
      <input v-model="keyWord" placeholder="搜索店员姓名/手机号" @keyup.enter="resetLoad" />
      <button @click="resetLoad">搜索</button>
      <button class="add-btn-top" @click="openAdd">新增店员</button>
    </div>

    <!-- 店员列表 -->
    <div class="list">
      <div class="item" v-for="item in list" :key="item.id">
        <div><strong>姓名：</strong>{{ item.realName || '无' }}</div>
        <div><strong>手机号：</strong>{{ item.phone || '无' }}</div>
        <div><strong>角色：</strong>{{ item.roleType === 1 ? '店长' : '店员' }}</div>
        <div><strong>状态：</strong>{{ item.status === 1 ? '正常' : '禁用' }}</div>
        <div><strong>创建时间：</strong>{{ item.createTime }}</div>
        <div class="btns">
          <button @click="openEdit(item)">编辑</button>
          <!-- 重置密码按钮 美化+间距 -->
          <button class="reset-btn" @click="doResetPwd(item.id)">重置密码</button>
          <button class="del-btn" @click="del(item.id)">删除</button>
        </div>
      </div>
    </div>

    <!-- 加载提示 -->
    <div class="load-tip" v-if="loading">加载中...</div>
    <div class="load-tip" v-if="noMore">没有更多了</div>

    <!-- 新增/编辑弹窗 -->
    <div class="popup-mask" v-if="showPopup" @click.self="closePopup">
      <div class="popup-content">
        <div class="popup-title">{{ isEdit ? '编辑店员' : '新增店员' }}</div>

        <div class="form-item">
          <text>店员姓名</text>
          <input v-model="form.realName" placeholder="请输入姓名" />
        </div>
        <div class="form-item">
          <text>手机号/账号</text>
          <input v-model="form.phone" placeholder="请输入手机号" />
        </div>
        <div class="form-item">
          <text>登录密码</text>
          <input v-model="form.password" placeholder="留空默认123456" />
        </div>
        <div class="form-item">
          <text>角色权限</text>
          <select v-model="form.roleType">
            <option :value="1">店长</option>
            <option :value="2">店员</option>
          </select>
        </div>
        <div class="form-item">
          <text>账号状态</text>
          <select v-model="form.status">
            <option :value="1">正常</option>
            <option :value="0">禁用</option>
          </select>
        </div>

        <div class="popup-btns">
          <button class="cancel-btn" @click.prevent="closePopup">取消</button>
          <button class="save-btn" @click.prevent="save">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getEmpList, saveEmp, deleteEmp, resetPwd } from '../../utils/api.js'

const router = useRouter()

const role = Number(localStorage.getItem('roleType'))
if (role !== 1) {
  alert('无权限访问')
  router.replace('/index')
}
const list = ref([])
const keyWord = ref('')
const pageNum = ref(1)
const pageSize = 10
const loading = ref(false)
const noMore = ref(false)

// 弹窗相关
const showPopup = ref(false)
const isEdit = ref(false)
const form = ref({
  id: '',
  realName: '',
  phone: '',
  password: '',
  roleType: 2,
  status: 1
})

// 重置搜索
const resetLoad = () => {
  pageNum.value = 1
  list.value = []
  noMore.value = false
  loadData()
}

// 加载店员列表
const loadData = async () => {
  if (loading.value || noMore.value) return
  loading.value = true

  try {
    const res = await getEmpList({
      keyWord: keyWord.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })

    const records = res?.data?.records || []
    if (pageNum.value === 1) {
      list.value = records
    } else {
      list.value.push(...records)
    }

    const total = res?.data?.total || 0
    if (list.value.length >= total) {
      noMore.value = true
    }
  } catch (e) {
    console.log("加载店员异常", e)
  } finally {
    loading.value = false
  }
}

// 滚动加载
const onScroll = () => {
  const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
  const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
  const clientHeight = document.documentElement.clientHeight
  if (scrollTop + clientHeight >= scrollHeight - 150) {
    pageNum.value++
    loadData()
  }
}

// 新增编辑弹窗
const openAdd = () => {
  isEdit.value = false
  form.value = {
    id: '',
    realName: '',
    phone: '',
    password: '',
    roleType: 2,
    status: 1
  }
  showPopup.value = true
}

const openEdit = (item) => {
  isEdit.value = true
  form.value = { ...item }
  showPopup.value = true
}

const closePopup = () => {
  showPopup.value = false
}

// 保存雇员
const save = async () => {
  if (!form.value.realName) {
    alert('请输入店员姓名')
    return
  }
  if (!form.value.phone) {
    alert('请输入手机号')
    return
  }
  await saveEmp(form.value)
  closePopup()
  resetLoad()
}

// 重置密码
const doResetPwd = async (id) => {
  if (!confirm('确定重置密码为 123456 吗？')) return
  await resetPwd(id)
  alert('密码已重置为：123456')
}

// 删除
const del = async (id) => {
  if (!confirm('确定删除该店员？')) return
  await deleteEmp(id)
  resetLoad()
}

onMounted(() => {
  loadData()
  window.addEventListener('scroll', onScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped>
.page{padding:20rpx;}
.search-box{display:flex;gap:10rpx;margin-bottom:20rpx;align-items:center;}
.search-box input{flex:1;padding:16rpx;border:1px solid #eee;border-radius:8rpx;}
.search-box button{background:#2d8cf0;color:#fff;border:none;padding:0 20rpx;border-radius:8rpx;}
.add-btn-top{background:#19be60 !important;}

.item{background:#fff;padding:20rpx;border-radius:12rpx;margin-bottom:15rpx;}
.btns{margin-top:10rpx;display:flex;gap:12rpx;}
.btns button{border:none;padding:8rpx 14rpx;border-radius:6rpx;font-size:24rpx;}
.reset-btn{background:#1890ff;color:#fff;}
.del-btn{background:#ff4d4f;color:#fff;}

.load-tip{text-align:center;padding:30rpx;color:#999;font-size:26rpx;}

/* 弹窗样式 */
.popup-mask{position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.5);display:flex;align-items:center;justify-content:center;z-index:999;}
.popup-content{width:85%;background:#fff;border-radius:12rpx;padding:30rpx;}
.popup-title{text-align:center;font-size:32rpx;font-weight:bold;margin-bottom:30rpx;}
.form-item{margin-bottom:20rpx;}
.form-item text{display:block;margin-bottom:10rpx;}
input,select{width:100%;padding:16rpx;border:1px solid #eee;border-radius:8rpx;box-sizing:border-box;}

.popup-btns{display:flex;gap:20rpx;margin-top:40rpx;}
.cancel-btn{flex:1;padding:16rpx;border:1px solid #eee;border-radius:8rpx;background:#f5f5f5;}
.save-btn{flex:1;padding:16rpx;border:none;border-radius:8rpx;background:#2d8cf0;color:#fff;}
</style>