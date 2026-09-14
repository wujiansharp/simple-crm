<template>
  <view>
    <input v-model="keyWord" placeholder="搜索客户姓名/电话" style="padding:20rpx;margin:20rpx;border:1px solid #eee;border-radius:12rpx;"/>
    <scroll-view scroll-y>
      <view class="item" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
        <view class="name">{{item.customerName}}</view>
        <view class="row">{{item.phone}}</view>
        <view class="row">分类：{{item.typeName}}</view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      keyWord:"",
      list:[]
    }
  },
  onShow() {
    this.getList()
  },
  methods: {
    async getList(){
      let res = await this.$api.get("/crm/customer/list",{
        keyWord:this.keyWord
      })
      if(res.code===200){
        this.list = res.data
      }
    },
    goDetail(id){
      uni.navigateTo({url:`/pages/customerAdd/customerAdd?id=${id}`})
    }
  }
}
</script>

<style scoped>
.item{background:#fff;margin:15rpx 20rpx;border-radius:12rpx;padding:25rpx;border:1px solid #f5f5f5;}
.name{font-size:30rpx;font-weight:bold;margin-bottom:10rpx;}
.row{font-size:26rpx;color:#666;margin-top:5rpx;}
</style>