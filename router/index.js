import { createRouter, createWebHistory } from 'vue-router'
import Index from '../pages/index/index.vue'
import Login from '../pages/login/login.vue'
import CustomerAdd from '../pages/customerAdd/customerAdd.vue'
import CustomerList from '../pages/customerList/index.vue'
import FollowList from '../pages/followList/index.vue'
import Follow from '../pages/follow/follow.vue'
import FollowWait from '../pages/follow/wait.vue'
import FollowAdd from '../pages/follow/add.vue'
import OrderAdd from '../pages/order/order.vue'
import OrderList from '../pages/orderList/index.vue'
import shopEdit from '../pages/shop/shopEdit.vue'
import employeeList from '../pages/employeeList/employeeList.vue'
import userEdit from '../pages/user/userEdit.vue'

const routes = [
  { path: '/', redirect: '/index' },
   { path: '/index', component: Index },
  { path: '/login', component: Login },
  { path: '/customerAdd', component: CustomerAdd },
  { path: '/customerList', component: CustomerList },
  { path: '/followList', component: FollowList },
  { path: '/follow', component: Follow },
  { path: '/followWait', component: FollowWait },
  { path: '/followAdd', component: FollowAdd },
  { path: '/orderAdd', component: OrderAdd },
  { path: '/orderList', component: OrderList },
  { path: '/shopEdit', component: shopEdit },
  { path: '/employeeList', component: employeeList },
   { path: '/userEdit', component: userEdit }
]

const router = createRouter({ history: createWebHistory(), routes })

// 登录拦截
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') return next()
  if (!token) return next('/login')
  next()
})

export default router