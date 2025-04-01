import { createApp } from 'vue'
import './style.css'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {createPinia} from 'pinia'
import  PiniaPlugin from 'pinia-plugin-persistedstate'
import App from './App.vue'
import * as ELIcons from '@element-plus/icons-vue'

const pinia=createPinia()
pinia.use(PiniaPlugin)

const app=createApp(App)
app.use(pinia)
app.use(router)
for (let iconName in ELIcons) {
    app.component(iconName, ELIcons[iconName])
  }
app.use(ElementPlus)

app.mount('#app')
