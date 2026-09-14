<template>
  <div class="home">
    <!-- 1. 数据统计卡片 -->
    <div class="card-wrap">
      <div class="card" @click="goPage('/customerList')">
        <span class="num">{{ count.customerNum }}</span>
        <span class="txt">总客户</span>
      </div>
      <div class="card orange" @click="goPage('/followWait')">
        <span class="num">{{ count.waitFollowNum }}</span>
        <span class="txt">待回访</span>
      </div>
      <div class="card green" @click="goPage('/orderList')">
        <span class="num">{{ count.monthOrderNum }}</span>
        <span class="txt">本月订单</span>
      </div>
    </div>

    <!-- 2. 功能入口 -->
    <div class="fun-list">
      <div class="fun-item" @click="goPage('/customerAdd')">
        <span>新增客户</span>
      </div>
      <div class="fun-item" @click="goPage('/follow')">
        <span>跟进记录</span>
      </div>
      <div class="fun-item" @click="goPage('/orderAdd')">
        <span>订单记账</span>
      </div>
    </div>

    <!-- 3. 我的商铺 -->
    <div class="section">
      <div class="title expand-title" @click="showShop = !showShop">
        我的商铺
        <text class="arrow">{{ showShop ? '▼' : '▶' }}</text>
      </div>
      <div class="info-card expand-body" v-show="showShop">
        <div class="info-row">
          <span>商铺名称：</span>
          <span>{{ shop.merchantName }}</span>
        </div>
        <div class="info-row">
          <span>联系电话：</span>
          <span>{{ shop.contactPhone }}</span>
        </div>
        <div class="info-row">
          <span>商铺地址：</span>
          <span>{{ shop.address }}</span>
        </div>
        <button class="edit-btn" @click="goEditShop">编辑商铺</button>
      </div>
    </div>

    <!-- 4. 我的信息 -->
    <div class="section">
      <div class="title expand-title" @click="showUser = !showUser">
        我的信息
        <text class="arrow">{{ showUser ? '▼' : '▶' }}</text>
      </div>
      <div class="info-card expand-body" v-show="showUser">
        <div class="info-row">
          <span>姓名：{{ user.name }}</span>
        </div>
        <div class="info-row">
          <span>角色：{{ user.role }}</span>
        </div>
        <button class="edit-btn" @click="goPage('/userEdit')">编辑我的信息</button>
        <button class="logout-btn" @click="logout">退出登录</button>
      </div>
    </div>

    <!-- 5. 我的雇员 -->
    <div class="section" v-if="showEmployeeMenu">
      <div class="title expand-title" @click="goPage('/employeeList')">
        我的店员
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHomeCount, getMyShop, getEmployeeList } from '../../utils/api.js'

const router = useRouter()

// 统计
const count = ref({ customerNum:0, waitFollowNum:0, monthOrderNum:0 })

// 控制展开收起
const showShop = ref(false)
const showUser = ref(false)

// 商铺
const shop = ref({ merchantName:'', contactPhone:'', address:'' })

// 我的信息
const user = ref({
  name: localStorage.getItem('realName') || '管理员',
  role: localStorage.getItem('roleName') || '店长'
})

// 角色判断
const role = ref(Number(localStorage.getItem('roleType')) || 0)
const showEmployeeMenu = ref(role.value === 1)

// 雇员
const empList = ref([])

// 加载
onMounted(async () => {
  const res = await getHomeCount()
  count.value = res.data || res

  const shopRes = await getMyShop()
  if (shopRes.data) shop.value = shopRes.data

  if (role.value === 1) {
    const empRes = await getEmployeeList()
    if (empRes.data) empList.value = empRes.data
  }
})

// 跳转
const goPage = (path) => router.push(path)
const goEditShop = () => router.push('/shopEdit')

// 退出
const logout = () => {
  if (confirm('确定退出？')) {
    localStorage.clear()
    router.push('/login')
  }
}
</script>

<style scoped>
/* 整体页面 统一风格 */
.home {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  box-sizing: border-box;
}

/* 统计卡片 */
.card-wrap{
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}
.card{
  flex: 1;
  background: #2d8cf0;
  border-radius: 12px;
  text-align: center;
  padding: 24px 12px;
  color:#fff;
  box-shadow: 0 4px 12px rgba(45, 140, 240, 0.15);
  cursor: pointer;
}
.card.orange {
  background: #ff7d00;
  box-shadow: 0 4px 12px rgba(255, 125, 0, 0.15);
}
.card.green {
  background: #00b42a;
  box-shadow: 0 4px 12px rgba(0, 180, 42, 0.15);
}
.num{
  font-size: 26px;
  font-weight: bold;
  display: block;
  margin-bottom: 6px;
}
.txt{
  font-size: 14px;
  opacity: 0.95;
}

/* 功能菜单 */
.fun-list{
  margin-bottom: 24px;
}
.fun-item{
  background:#fff;
  margin-bottom:12px;
  border-radius:12px;
  padding:20px;
  border:1px solid #f0f2f5;
  font-size: 15px;
  color: #333;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  cursor: pointer;
}

/* 模块 */
.section {
  margin-bottom: 20px;
}
.expand-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size:16px;
  font-weight:bold;
  color:#333;
  padding:16px 20px;
  background:#fff;
  border-radius:12px;
  border:1px solid #f0f2f5;
  cursor:pointer;
}
.arrow{
  color:#999;
  font-size:14px;
}
.expand-body{
  margin-top:8px;
  background:#fff;
  border-radius:12px;
  padding:20px;
  border:1px solid #f0f2f5;
}

.info-row {
  font-size:15px;
  line-height:30px;
  color:#333;
}

/* 按钮 */
.edit-btn {
  width: 100%;
  margin-top:16px;
  padding:14px;
  background:#2d8cf0;
  color:#fff;
  border:none;
  border-radius:8px;
  font-size: 15px;
  cursor: pointer;
}
.logout-btn {
  width: 100%;
  margin-top:12px;
  padding:14px;
  background:#ff4d4f;
  color:#fff;
  border:none;
  border-radius:8px;
  font-size: 15px;
  cursor: pointer;
}
</style>