<template>
    <el-container class="layout-container-demo" style="height: 100%;">
      <el-aside :width="menuWidth" style="border-right: black solid 1px;height: 100%;" >
        <el-scrollbar>
          <el-menu unique-opened router :collapse="isCollapse" :collapse-transition="false">
            <el-sub-menu index="1">
              <template #title>
                <el-icon><message /></el-icon><span>XX邮件</span>
              </template>
            <el-menu-item index="/main/closet">表格显示</el-menu-item>
            <el-menu-item index="1-2">草稿栏</el-menu-item>
            <el-menu-item index="1-3">已发送</el-menu-item>
              <el-sub-menu index="1-4">
                <template #title>垃圾消息</template>
                <el-menu-item index="1-4-1">Option 4-1</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>
                <el-icon><icon-menu /></el-icon><span>XX功能</span>
              </template>
                <el-menu-item index="2-1">Option 1</el-menu-item>
                <el-menu-item index="2-2">Option 2</el-menu-item>
                <el-menu-item index="2-3">Option 3</el-menu-item>
              <el-sub-menu index="2-4">
                <template #title>Option 4</template>
                <el-menu-item index="2-4-1">Option 4-1</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
            <el-sub-menu index="3">
              <template #title> 
                <el-icon><setting /></el-icon><span>XX设置</span>
              </template>
                <el-menu-item index="3-1">Option 1</el-menu-item>
                <el-menu-item index="3-2">Option 2</el-menu-item>
                <el-menu-item index="3-3">Option 3</el-menu-item>
              <el-sub-menu index="3-4">
                <template #title>Option 4</template>
                <el-menu-item index="3-4-1">Option 4-1</el-menu-item>
              </el-sub-menu>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-aside>
  
      <el-container>
        <el-header style="font-size: 12px">
          <div class="toolbar" style="display: flex;flex-direction: row-reverse;justify-content: flex-start;">
            
            <span >Tom</span>
            <el-dropdown >
              <el-icon style="margin-right: 8px; margin-top: 1px">
                <setting />
              </el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>View</el-dropdown-item>
                  <el-dropdown-item>Add</el-dropdown-item>
                  <el-dropdown-item @click="exit">Delete</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <span @click="changecollapse" style="margin-right: auto;" ><el-icon :size="20" v-if="!isCollapse" ><Expand/></el-icon>
            <el-icon :size="20" v-else ><Fold/></el-icon>{{ menuTitle }}
            </span>
          </div>
        </el-header>
  
        <el-main>
          <el-scrollbar>
            <router-view></router-view>
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </template>
  
  <script lang="js" setup>
  import { ref, watch } from 'vue'
  import { Menu as IconMenu, Message, Setting, Expand, Fold } from '@element-plus/icons-vue'
  import { useRoute,useRouter } from 'vue-router';
  const menuTitle = ref('')
  const route=useRoute()
  
  watch(route,()=>{
    console.log(route)
    menuTitle.value=route.meta.title
},{deep:true,immediate:true}) 
  const router = useRouter();
  let isCollapse = ref(false)
  const menuWidth = ref('200px')
  const changecollapse = () => {
    isCollapse.value = !isCollapse.value
    menuWidth.value = isCollapse.value ? '64px' : '200px'
  }
  const exit = () => {
    localStorage.removeItem('taken')
    router.replace('/login')
  }
  </script>
  
  <style scoped>
  .layout-container-demo .el-header {
    position: relative;
    background-color: var(--el-color-primary-light-7);
    color: var(--el-text-color-primary);
  }
 
  .layout-container-demo .el-menu {
    border-right: none;
  }
  .layout-container-demo .el-main {
    padding: 0;
  }
  .layout-container-demo .toolbar {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    right: 20px;
  }
  </style>
  