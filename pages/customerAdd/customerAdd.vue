<template>
  <div class="customer-add">
    <div class="form-item">
      <label>客户姓名</label>
      <input v-model="form.customerName" placeholder="请输入" />
    </div>

    <div class="form-item">
      <label>联系电话</label>
      <input v-model="form.phone" placeholder="请输入" />
    </div>

    <div class="form-item">
      <label>客户分类</label>
      <select v-model="form.typeId">
        <option value="">请选择分类</option>
        <option v-for="item in typeList" :key="item.id" :value="item.id">
          {{ item.typeName }}
        </option>
      </select>
    </div>

    <div class="form-item">
      <label>备注</label>
      <textarea v-model="form.remark" placeholder="请输入备注"></textarea>
    </div>

    <button class="save-btn" @click="save" :disabled="saving">
      {{ saving ? '保存中...' : (isEdit ? '更新' : '保存') }}
    </button>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCustomerTypeList, saveCustomer, getCustomerInfo } from '../../utils/api.js'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const isEdit = ref(false)  // 是否是编辑模式
const customerId = ref('')  // 客户ID

const form = reactive({
  customerName: '',
  phone: '',
  typeId: '',
  remark: ''
})

const typeList = ref([])

// 页面加载
onMounted(async () => {
  // 1. 先加载分类列表
  await loadTypeList()

  // 2. 检查是否有 id 参数（编辑模式）
  const id = route.query.id
  if (id) {
    isEdit.value = true
    customerId.value = id
    await loadCustomerInfo(id)
  }
})

// 加载分类
const loadTypeList = async () => {
  try {
    const res = await getCustomerTypeList()
    console.log('分类返回:', res)

    if (res?.code === 200) {
      typeList.value = res.data || []
    } else if (res?.data) {
      typeList.value = res.data
    } else if (Array.isArray(res)) {
      typeList.value = res
    }
  } catch (error) {
    console.error('加载分类失败:', error)
    alert('加载分类失败')
  }
}

// 加载客户详情（编辑时）
const loadCustomerInfo = async (id) => {
  try {
    const res = await getCustomerInfo(id)
    console.log('客户详情返回:', res)

    const data = res?.data || res
    if (data) {
      form.customerName = data.customerName || ''
      form.phone = data.phone || ''
      form.typeId = data.typeId || ''
      form.remark = data.remark || ''
    }
  } catch (error) {
    console.error('加载客户详情失败:', error)
    alert('加载客户详情失败')
  }
}

// 保存
const save = async () => {
  if (!form.customerName?.trim()) {
    alert('请输入客户姓名')
    return
  }
  if (!form.typeId) {
    alert('请选择客户分类')
    return
  }

  saving.value = true

  // 构建提交数据
  const submitData = {
    customerName: form.customerName.trim(),
    phone: form.phone,
    typeId: form.typeId,
    remark: form.remark
  }

  // 编辑模式需要传 id
  if (isEdit.value && customerId.value) {
    submitData.id = customerId.value
  }

  try {
    const res = await saveCustomer(submitData)
    console.log('保存返回:', res)

    if (res?.code === 200) {
      alert(isEdit.value ? '更新成功' : '保存成功')
      router.back()
    } else {
      alert(res?.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.customer-add {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}
.form-item {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
label {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}
input, select, textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  font-family: inherit;
}
input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: #2d8cf0;
}
textarea {
  min-height: 100px;
  resize: vertical;
}
.save-btn {
  width: 100%;
  padding: 12px 24px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  margin-top: 20px;
  cursor: pointer;
}
.save-btn:hover {
  opacity: 0.85;
}
.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>