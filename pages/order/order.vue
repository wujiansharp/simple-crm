<template>
  <div class="order-add">
    <div class="form-item">
      <label>选择客户 *</label>
      <select v-model="form.customerId">
        <option value="">请选择客户</option>
        <option :value="item.id" v-for="item in customerList" :key="item.id">
          {{ item.customerName }}
        </option>
      </select>
    </div>

    <div class="form-item">
      <label>订单编号</label>
      <input v-model="form.orderNo" disabled placeholder="自动生成" />
    </div>

    <div class="form-item">
      <label>支付状态</label>
      <select v-model="form.payStatus">
        <option :value="0">未支付</option>
        <option :value="1">已支付</option>
      </select>
    </div>

    <div class="form-item">
      <label>下单时间</label>
      <input type="datetime-local" v-model="form.orderTime" />
    </div>

    <div class="title">商品列表</div>
    <div class="goods-list">
      <div class="goods-item" v-for="(item,idx) in goodsList" :key="idx">
        <select v-model="item.goodsId" @change="selectGoods(item,idx)">
          <option value="">选择商品</option>
          <option :value="g.id" v-for="g in goodsListAll" :key="g.id">
            {{ g.goodsName }} - ¥{{ g.price }}
          </option>
        </select>

        <input v-model="item.goodsName" placeholder="商品名称" />
        <input type="number" v-model="item.price" placeholder="单价" @input="calcItem(item)" />
        <input type="number" v-model="item.num" placeholder="数量" @input="calcItem(item)" />
        <input v-model="item.totalPrice" disabled placeholder="小计" />
        <button class="del" @click="goodsList.splice(idx,1)">删除</button>
      </div>

      <button class="add-btn" @click="addGoods">+ 添加商品</button>
    </div>

    <div class="total">
      实付金额：¥{{ totalAmount }}
    </div>

    <button class="save-btn" @click="save">提交订单</button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderInfo, saveOrder } from '../../utils/api.js'
import { queryCustomerList } from '../../utils/api.js'
import { getGoodsList } from '../../utils/api.js'

const route = useRoute()
const router = useRouter()
const id = ref(route.query.id || '')

const form = ref({
  id: '',
  customerId: '',
  orderNo: '',
  payStatus: 0,
  orderTime: ''
})

const customerList = ref([])
const goodsListAll = ref([])
const goodsList = ref([])

const addGoods = () => {
  goodsList.value.push({
    goodsId: '',
    goodsName: '',
    price: 0,
    num: 1,
    totalPrice: 0
  })
}

const selectGoods = (item) => {
  const g = goodsListAll.value.find(x => x.id == item.goodsId)
  if (g) {
    item.goodsName = g.goodsName
    item.price = g.price
    calcItem(item)
  }
}

const calcItem = (item) => {
  item.totalPrice = (Number(item.price) || 0) * (Number(item.num) || 0)
}

const totalAmount = computed(() => {
  return goodsList.value.reduce((t, i) => t + (Number(i.totalPrice) || 0), 0).toFixed(2)
})

onMounted(async () => {
  const resCus = await queryCustomerList()
  customerList.value = resCus.data

  const resGoods = await getGoodsList()
  goodsListAll.value = resGoods.data

  if (id.value) {
    const detail = await getOrderInfo(id.value)
    console.log("详情数据", detail.data)
    form.value = detail.data

    if (detail.data.goodsList && detail.data.goodsList.length > 0) {
      goodsList.value = detail.data.goodsList
    } else {
      addGoods()
    }
  } else {
    addGoods()
  }
})

const save = async () => {
  if (!form.value.customerId) {
    alert('请选择客户')
    return
  }
  form.value.orderAmount = totalAmount.value
  await saveOrder({
    order: form.value,
    goodsList: goodsList.value
  })
  router.back()
}
</script>
<style scoped>
/* 完全和你的客户编辑页样式统一 */
.order-add {
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

.title {
  font-size: 16px;
  font-weight: 500;
  margin: 20px 0 10px;
  color: #333;
}

/* ========== 商品列表改成两行布局 ========== */
.goods-item {
  background: #f9fafb;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.goods-item select {
  width: 100%;
}
.goods-item input {
  flex: 1;
  min-width: 100px;
}

.del {
  background: #ff4d4f;
  color: #fff;
  border: none;
  padding: 12px 10px;
  border-radius: 8px;
  font-size: 14px;
  white-space: nowrap;
}

.add-btn {
  width: 100%;
  padding: 12px;
  background: #f7f8fa;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 10px 0;
  font-size: 14px;
}

.total {
  text-align: right;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 0;
}

.save-btn {
  width: 100%;
  padding: 12px 24px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  margin-top: 10px;
  cursor: pointer;
}
.save-btn:hover {
  opacity: 0.85;
}
</style>