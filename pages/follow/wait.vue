<template>
  <div class="follow-wait">
    <!-- 顶部新增按钮 -->
    <div class="search-box">
      <button class="add-btn" @click="goAdd">新增跟进</button>
    </div>

    <div class="list">
      <div class="item" v-for="item in list" :key="item.id">
        <div class="title">{{ item.customerName }}</div>
        <div class="content">{{ item.followContent }}</div>
        <div class="next">下次回访：{{ item.nextFollowTime }}</div>
        <div class="btns">
          <button class="edit-btn" @click="goEdit(item.id)">跟进</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getWaitFollowList } from '../../utils/api.js'

const router = useRouter()
const list = ref([])

onMounted(async () => {
  const res = await getWaitFollowList()
  list.value = res.data
})

const goAdd = () => {
  router.push('/followAdd')
}

const goEdit = (id) => {
  router.push(`/followAdd?id=${id}`)
}
</script>

<style scoped>
.follow-wait {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

/* 顶部新增按钮 和其他页面统一 */
.search-box {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
.add-btn {
  padding: 12px 20px;
  background: #2d8cf0;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.list .item {
  background: #fff;
  padding: 16px;
  margin-bottom: 12px;
  border-radius: 8px;
  border: 1px solid #eee;
}

.title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
}

.content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 10px;
}

.next {
  font-size: 12px;
  color: #ff4d4f;
  font-weight: 500;
}

.btns {
  margin-top: 14px;
}
.edit-btn {
  padding: 8px 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  background: #ff7d00;
  color: #fff;
}
</style>