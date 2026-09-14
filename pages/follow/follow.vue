<template>
  <div class="follow-list">
    <!-- 顶部：新增按钮 -->
    <div class="search-box">
      <button @click="goAdd" class="add-btn">新增跟进</button>
    </div>

    <div class="list">
      <div class="item" v-for="item in list" :key="item.id">
        <div class="title">{{ item.customerName }}</div>
        <div class="content">{{ item.followContent }}</div>
        <div class="time">跟进时间：{{ item.followTime }}</div>
        <div class="next" v-if="item.nextFollowTime">
          下次回访：{{ item.nextFollowTime }}
        </div>
        <div class="btns">
          <button class="edit-btn" @click="goEdit(item.id)">编辑</button>
          <button class="del-btn" @click="del(item.id)">删除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getFollowList, deleteFollow } from '../../utils/api.js'

const router = useRouter()
const list = ref([])

onMounted(async () => {
  const res = await getFollowList()
  list.value = res.data
})

const goAdd = () => {
  router.push('/followAdd')
}

const goEdit = (id) => {
  router.push(`/followAdd?id=${id}`)
}

const del = async (id) => {
  if (!confirm('确定删除？')) return
  await deleteFollow(id)
  const res = await getFollowList()
  list.value = res.data
}
</script>

<style scoped>
.follow-list {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

/* 顶部新增按钮 和你其他页面样式统一 */
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

.time,
.next {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

/* 编辑删除按钮 和订单列表一模一样 */
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
</style>