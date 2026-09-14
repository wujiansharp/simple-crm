<template>
  <div class="page">
    <div class="form-item">
      <text>商铺名称</text>
      <input v-model="form.merchantName" placeholder="请输入商铺名称" />
    </div>

    <div class="form-item">
      <text>联系电话</text>
      <input v-model="form.contactPhone" placeholder="请输入联系电话" />
    </div>

    <div class="form-item">
      <text>商铺地址</text>
      <input v-model="form.address" placeholder="请输入商铺地址" />
    </div>

    <div class="form-item">
      <text>营业时间</text>
      <input v-model="form.businessHours" placeholder="例如：9:00-21:00" />
    </div>

    <div class="form-item">
      <text>备注</text>
      <textarea v-model="form.remark" placeholder="请输入备注"></textarea>
    </div>

    <button class="save-btn" @click="save">保存商铺</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyShop, saveShop } from '../../utils/api.js'

const router = useRouter()
const form = ref({
  id: '',
  merchantName: '',
  contactPhone: '',
  address: '',
  businessHours: '',
  remark: ''
})

// 进入页面就加载我的商铺（有就回显，没有就是新增）
onMounted(async () => {
  const res = await getMyShop()
  if (res.data) {
    form.value = res.data
  }
})

// 保存
const save = async () => {
  if (!form.value.merchantName) {
    alert('请输入商铺名称')
    return
  }

  await saveShop(form.value)
  alert('保存成功')
  router.back()
}
</script>

<style scoped>
.page {
  padding: 20rpx;
}
.form-item {
  background: #fff;
  margin-bottom: 20rpx;
  padding: 20rpx;
  border-radius: 10rpx;
}
.form-item text {
  font-size: 28rpx;
  margin-bottom: 10rpx;
  display: block;
}
input, textarea {
  width: 100%;
  padding: 15rpx;
  border: 1px solid #eee;
  border-radius: 8rpx;
  font-size: 28rpx;
}
textarea {
  min-height: 120rpx;
}
.save-btn {
  width: 100%;
  background: #2d8cf0;
  color: #fff;
  padding: 24rpx;
  border-radius: 10rpx;
  font-size: 30rpx;
  border: none;
  margin-top: 20rpx;
}
</style>