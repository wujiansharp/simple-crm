<template>
  <div class="order-list">
    <!-- 搜索 + 新增 -->
    <div class="search-box">
      <input v-model="customerName" placeholder="搜索客户名称" @keyup.enter="resetLoad" />
      <button @click="resetLoad">搜索</button>
      <button class="add-btn" @click="goAdd">新增</button>
    </div>

    <!-- 订单列表 -->
    <div class="list">
      <div class="item" v-for="item in list" :key="item.id">
        <div><strong>客户：</strong>{{ item.customerName || '无' }}</div>
        <div>订单号：{{ item.orderNo }}</div>
        <div>下单时间：{{ item.orderTime }}</div>
        <div>合计金额：¥{{ item.orderAmount }}</div>
        <div>状态：
          <span :class="item.payStatus === 1 ? 'status-paid' : 'status-unpaid'">
            {{ item.payStatus === 1 ? '已支付' : '未支付' }}
          </span>
        </div>

        <!-- 商品列表 -->
        <div class="goods-group" v-if="item.goodsList && item.goodsList.length">
          <div class="goods-title">商品列表</div>
          <div class="goods-item" v-for="(g, idx) in item.goodsList" :key="idx">
            <div>商品：{{ g.goodsName }}</div>
            <div>单价：¥{{ g.price }}</div>
            <div>数量：{{ g.num }}</div>
            <div>小计：¥{{ g.totalPrice }}</div>
          </div>
        </div>

        <div class="btns">
          <button class="edit-btn" @click="goEdit(item.id)">编辑</button>
          <button class="del-btn" @click="del(item.id)">删除</button>
        </div>
      </div>
    </div>

    <!-- 加载提示 -->
    <div class="load-tip" v-if="loading">加载中...</div>
    <div class="load-tip" v-if="noMore">没有更多了</div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrderList, deleteOrder } from '../../utils/api.js'

const router = useRouter()

const list = ref([])
const customerName = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const noMore = ref(false)

// 重置搜索
const resetLoad = () => {
  pageNum.value = 1
  list.value = []
  noMore.value = false
  loadData()
}

// 加载数据
const loadData = async () => {
  if (loading.value || noMore.value) return
  loading.value = true

  try {
    const res = await getOrderList({
      customerName: customerName.value,
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
    console.error('加载失败', e)
  } finally {
    loading.value = false
  }
}

// 滚动加载
const onScroll = () => {
  const st = document.documentElement.scrollTop || document.body.scrollTop
  const sh = document.documentElement.scrollHeight || document.body.scrollHeight
  const ch = document.documentElement.clientHeight
  if (st + ch + 150 >= sh) {
    pageNum.value++
    loadData()
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('scroll', onScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})

// 新增、编辑、删除
const goAdd = () => router.push('/orderAdd')
const goEdit = (id) => router.push(`/orderAdd?id=${id}`)

const del = async (id) => {
  if (!confirm('确定删除？')) return
  await deleteOrder(id)
  resetLoad()
}
</script>

<style scoped>
/* 整体统一风格 */
.order-list {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

/* 搜索栏 */
.search-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.search-box input {
  flex: 1;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
}
.search-box input:focus {
  outline: none;
  border-color: #2d8cf0;
}
.search-box button {
  padding: 12px 20px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
}
.search-box .add-btn {
  background: #00b42a;
}

/* 订单卡片 */
.item {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #eee;
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.7;
}

/* 支付状态 */
.status-paid {
  color: #00b42a;
  font-weight: 500;
}
.status-unpaid {
  color: #ff7d00;
  font-weight: 500;
}

/* 商品组 */
.goods-group {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed #eee;
}
.goods-title {
  font-weight: 500;
  margin-bottom: 8px;
  color: #333;
}
.goods-item {
  padding-left: 8px;
  color: #666;
  line-height: 1.6;
}

/* 按钮 */
.btns {
  margin-top: 14px;
  display: flex;
  gap: 10px;
}
.btns button {
  padding: 8px 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}
.edit-btn {
  background: #ff7d00;
  color: #fff;
}
.del-btn {
  background: #ff4d4f;
  color: #fff;
}

/* 加载提示 */
.load-tip {
  text-align: center;
  padding: 20px 0;
  color: #999;
  font-size: 14px;
}
</style>