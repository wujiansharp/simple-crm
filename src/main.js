import { createApp } from 'vue'
import App from '../App.vue'

// 如果你想提前引入 api.js 工具
//import '../utils/api.js'
import router from '../router/index.js'
createApp(App).use(router).mount('#app')