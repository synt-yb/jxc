import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import Header from './views/components/header.vue'
import { createPinia } from 'pinia'
const pinia = createPinia()
import piniaPlugin from 'pinia-plugin-persistedstate'
pinia.use(piniaPlugin)

const app=createApp(App)
app.use(router)
app.component('Header',Header)
app.use(ElementPlus)
app.use(pinia)
app.mount('#app')

