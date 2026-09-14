<template>
  <div class="page">
    <div class="form-item">
      <text>客户 *</text>
      <select v-model="form.customerId">
        <option value="">请选择客户</option>
        <option :value="item.id" v-for="item in customerList" :key="item.id">
          {{ item.customerName }}
        </option>
      </select>
    </div>
    <div class="form-item">
      <text>跟进内容 *</text>
      <textarea v-model="form.followContent" placeholder="请输入跟进内容"></textarea>
    </div>
    <div class="form-item">
      <text>跟进时间</text>
      <input type="datetime-local" v-model="form.followTime" />
    </div>
    <div class="form-item">
      <text>下次回访时间</text>
      <input type="datetime-local" v-model="form.nextFollowTime" />
    </div>

    <button class="save-btn" @click="save">保存</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { saveFollow, getFollowDetail } from '../../utils/api.js'
import { queryCustomerList } from '../../utils/api.js'

const router = useRouter()
const route = useRoute()
const id = ref(route.query.id || '')

const form = ref({
  id: '',
  customerId: '',
  customerName: '',
  followContent: '',
  followTime: '',
  nextFollowTime: ''
})

const customerList = ref([])

onMounted(async () => {
  // 加载客户列表
  const res = await queryCustomerList()
  customerList.value = res.data

  // 编辑模式加载详情
  if (id.value) {
    const detail = await getFollowDetail(id.value)
    form.value = detail.data
  } else {
    // 新增默认跟进时间为当前时间
    form.value.followTime = new Date().toISOString().slice(0, 16)
  }
})

const save = async () => {
  if (!form.value.customerId || !form.value.followContent) {
    alert('请填写必填项')
    return
  }
  // 自动设置客户名称
  const c = customerList.value.find(x => x.id == form.value.customerId)
  if (c) form.value.customerName = c.customerName

  await saveFollow(form.value)
  alert('保存成功')
  router.back()
}
</script>

<style scoped>
.page { padding: 20px; }
.form-item { margin-bottom: 15px; }
.form-item text { display: block; margin-bottom: 5px; }
select, textarea, input { width: 100%; padding: 8px; border: 1px solid #eee; border-radius: 4px; }
textarea { min-height: 100px; }
.save-btn { width: 100%; background: #2d8cf0; color: #fff; border: none; padding: 10px; border-radius: 4px; margin-top: 15px; }
</style>