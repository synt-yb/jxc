<template>
  <div style="height: 100%;background-color: #EBF1F6;">
    <el-container style="height: 100%;">
      <el-header style="height: 40px;">
        <div height="40px" style="display: flex; align-items: center;font-size: 16px;">
          <img src="../../assets/adminLogo.png" style="height: 30px;width: 30px;margin-top: 5px;" />
          <span style="font-size: 16px;font-weight: bold;color: #409EFF;line-height: 40px;height: 40px;">后台管理系统</span>
          <span style="margin-left: auto;color: #616469;">你好 {{ useUserStore.user.name }}
            <el-button type="primary" bg plain :icon="SwitchButton" @click="logout">退出</el-button>
          </span>
        </div>
      </el-header>
      <el-container style="height: 100%;">
        <el-aside width="200px" style="height: 100%;">

          <el-menu router class="menu" unique-opened :default-active="useMenuStore.active_index">
            <el-sub-menu :index="item.id" v-for="item in useMenuStore.showlist">
              <template #title>
                <el-icon>
                  <component :is="strToObj(item.icon)" />
                </el-icon>
                <span>{{ item.text }}</span>
              </template>
              <el-menu-item :index="element.url" v-for="element in item.children">
                <el-icon>
                  <!-- <component :is="strToObj(element.icon)" /> -->
                  <component :is="element.icon" />
                </el-icon>
                <span>{{ element.text }}</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main style="background-color: white; padding: 0px;height: 100%;">
          <div class="tag-box">
            <div v-for="item,index in useMenuStore.tags" @click="goTo(item.url)">
              <span :class="{tag_com: !item.selected, tag_selected: item.selected}">{{ item.text }}</span>
              <el-button  :icon="Close" v-if="item.deletable" class="btn_close" @click.stop="delTag(index,item.selected)"/>
            </div>

          </div>
          <div style="height: 94%;">
            <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { SwitchButton, Document, Loading, FolderOpened } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router';
import { userStore } from '../../store/user'
import { menuStore } from '../../store/menu'
import http from '../../untils/http';
import { Close } from '@element-plus/icons-vue'
//变量池
const useUserStore = userStore()
const useMenuStore = menuStore()
const router = useRouter()



//  初始化
onMounted(() => {
  //  查询有哪些菜单
  if(useMenuStore.tags.length != 0)
  return
  http.post('/menu/selectByUser', {
    id: useUserStore.user.id
  }).then(res => {
    if (res.code == 200) {
      
      useMenuStore.setPermitMenus(res.data)
      router.push(useMenuStore.permitMenus[0])
    }
  })
})
// 点击上方标题栏
const goTo=(url)=>{
  router.push(url)
}
// 点击上方的标题栏删除键
const delTag = (index,selected) => {
  console.log("第"+index+"个标签页,选中状态:"+selected)
  useMenuStore.delTag(index,selected)
 
}

//todo 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('menuStore')
  useUserStore.clearUser()
  router.replace('/login')
}

//字符串转对象
const strToObj = (str) => {
  switch (str) {
    case 'Document':
      return Document
    case 'FolderOpened':
      return FolderOpened
    default:
      return Loading
  }
}


</script>
<style scoped>
/* 退出按钮 样式 */
:deep(.el-button) {
  height: 100%;
  padding-left: 10px;
  margin-left: 10px;
  /*background-color: transparent;*/
  border: none;
}

/* 侧边栏菜单 样式 */
:deep(.el-sub-menu__title),
:deep(.el-menu-item) {
  background-color: #EBF1F6;
}

.menu span::selection {
  background-color: transparent;
  color: inherit;
}

.tag-box {
  height: 40px;
  display: flex;
  margin: 0px;
  border-bottom: lightgray 1px solid;
  cursor: pointer;
}

.tag-box div {
  padding-left: 10px;
  padding-right: 10px;
  line-height: 40px;
  border: 1px solid #ccc;
  border-bottom: 2px solid white;
}
.tag-box span::selection {
  background-color: transparent;
  color: inherit;
}
.btn_close{
  height: 20px;
  width: 20px;
  margin: 0px;
  padding: 2px;
  border-radius: 20px;
}
.tag_com{
  font-size: 14px;
 
}
.tag_selected{
  font-size: 14px;
  color: skyblue;
}
</style>