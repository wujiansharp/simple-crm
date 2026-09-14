<template>
  <div class="page">
    <div class="top">
      <span>跟进记录</span>
      <button @click="goAdd">新增跟进</button>
    </div>

    <div class="list">
      <div class="item" v-for="item in list" :key="item.id">
        <div class="title">{{ item.customerName }}</div>
        <div class="content">{{ item.followContent }}</div>
        <div class="time">跟进时间：{{ item.followTime }}</div>
        <div class="next" v-if="item.nextFollowTime">
          下次回访：{{ item.nextFollowTime }}
        </div>
        <div class="opt">
          <span @click="goEdit(item.id)">编辑</span>
          <span @click="del(item.id)">删除</span>
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
.page { padding: 20px; }
.top { display: flex; justify-content: space-between; margin-bottom: 15px; }
.top button { background: #2d8cf0; color: #fff; border: none; padding: 6px 12px; border-radius: 4px; }
.list .item { background: #fff; padding: 15px; margin-bottom: 10px; border-radius: 8px; border: 1px solid #eee; }
.title { font-weight: bold; margin-bottom: 5px; }
.content { color: #666; margin-bottom: 5px; }
.time, .next { font-size: 12px; color: #999; }
.opt { margin-top: 8px; }
.opt span { color: #2d8cf0; margin-right: 10px; cursor: pointer; }
</style>