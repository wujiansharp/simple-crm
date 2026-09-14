<template>
  <div class="customer-list">
    <!-- 搜索栏 -->
    <div class="search-box">
      <input
        v-model="keyWord"
        placeholder="搜索客户姓名"
        @keyup.enter="loadList"
      />
      <button @click="loadList">搜索</button>
      <button @click="toAdd" class="add-btn">新增</button>
    </div>

    <!-- 列表 -->
    <div class="list">
      <div class="item" v-for="item in showList" :key="item.id">
        <div>
          <div class="name">{{ item.customerName }}</div>
          <div class="phone">{{ item.phone }}</div>
          <div class="type" v-if="item.typeName">{{ item.typeName }}</div>
        </div>
        <div class="btns">
          <button class="edit-btn" @click="toEdit(item.id)">编辑</button>
          <button class="del-btn" @click="doDelete(item.id)">删除</button>
        </div>
      </div>
    </div>

    <!-- 加载提示 -->
    <div class="load-more" v-if="loading">加载中...</div>
    <div class="load-more empty" v-if="isEnd && !loading && showList.length > 0">
      没有更多了
    </div>
    <div class="empty-data" v-if="!loading && showList.length === 0">
      暂无数据
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { queryCustomerList, deleteCustomer } from '../../utils/api.js'

const router = useRouter()

const allList = ref([])
const showList = ref([])
const keyWord = ref('')

const pageSize = 10
const pageIndex = ref(1)
const loading = ref(false)
const isEnd = ref(false)

onMounted(() => {
  loadList()
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

async function loadList() {
  if (loading.value) return

  loading.value = true
  pageIndex.value = 1
  isEnd.value = false

  try {
    const res = await queryCustomerList(keyWord.value)
    console.log('列表返回:', res)

    allList.value = res.data || res || []
    showList.value = allList.value.slice(0, pageSize)
    pageIndex.value++
    checkEnd()
  } catch (error) {
    console.error('加载列表失败:', error)
    alert('加载列表失败')
  } finally {
    loading.value = false
  }
}

function handleScroll() {
  const scrollTop = document.documentElement.scrollTop || document.body.scrollTop
  const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight
  const clientHeight = document.documentElement.clientHeight

  if (scrollTop + clientHeight >= scrollHeight - 50) {
    if (!loading.value && !isEnd.value) {
      loadMore()
    }
  }
}

function loadMore() {
  if (loading.value || isEnd.value) return

  loading.value = true

  const start = (pageIndex.value - 1) * pageSize
  const end = pageIndex.value * pageSize
  const nextArr = allList.value.slice(start, end)

  if (nextArr.length > 0) {
    showList.value = [...showList.value, ...nextArr]
    pageIndex.value++
  }

  loading.value = false
  checkEnd()
}

function checkEnd() {
  if (showList.value.length >= allList.value.length) {
    isEnd.value = true
  }
}

async function doDelete(id) {
  if (!confirm('确定删除该客户吗？')) return

  try {
    const res = await deleteCustomer(id)
    if (res.code === 200) {
      alert('删除成功')
      loadList()
    } else {
      alert(res.msg || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败')
  }
}

function toAdd() {
  router.push('/customerAdd')
}

function toEdit(id) {
  router.push(`/customerAdd?id=${id}`)
}
</script>

<style scoped>
/* 完全统一你的项目风格 */
.customer-list {
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
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
}
.search-box button:hover {
  opacity: 0.85;
}
.add-btn {
  background: #00b42a !important;
}

/* 列表项 */
.item {
  padding: 16px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.item:hover {
  border-color: #2d8cf0;
}

.name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 6px;
  color: #333;
}
.phone {
  font-size: 14px;
  color: #666;
}
.type {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

/* 按钮 */
.btns {
  display: flex;
  gap: 8px;
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
  color: white;
}
.del-btn {
  background: #ff4d4f;
  color: white;
}

/* 空数据 / 加载 */
.load-more {
  text-align: center;
  padding: 20px 0;
  color: #999;
  font-size: 14px;
}
.empty-data {
  text-align: center;
  padding: 60px 0;
  color: #999;
  font-size: 14px;
}
</style>