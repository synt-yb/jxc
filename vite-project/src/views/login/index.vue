<template>
  
    <div id="outer">
   <div id="login_box">
    <h2>LOGIN</h2>
    <div id="input_box" >
    <div>
    <el-input
      v-model="user.account"
      style="width: 240px"
      placeholder="请输入账号"
      :prefix-icon="User"
      class="el-input"
    />
</div>
<div>
    <el-input
      v-model="user.password"
      type="password"
      style="width: 240px"
      placeholder="请输入密码"
      show-password
      :prefix-icon="Lock"
    />
</div><div>
     <el-checkbox v-model="isRemember" label="记住密码" size="large" />
    </div>
     <div style="text-align: center;margin-top: 10px;margin-top: 20px;margin-bottom: 20px;">
        <el-button color="#3194B1"  style="width: 240px;" @click="login" >登录</el-button>
     </div>
    </div>
    
  </div>
  </div>
</template>
<script setup>
//导入
 import { useRouter } from 'vue-router';
 import { ref,onMounted } from 'vue';
 import { Unlock,Lock, User } from '@element-plus/icons-vue'
 import { ElMessage } from 'element-plus'
 import http from '../../untils/http'
 import {userStore} from '../../store/user'
 import {menuStore} from '../../store/menu'
 //变量池
 const useUserStore=userStore()
 const useMenuStore=menuStore()
 const user=ref({account:'',password:''})
 const router = useRouter()
 let isRemember=ref(true)
 //登录页面初始化
 onMounted(()=>{
    user.value.account=useUserStore.userRemember.account
    user.value.password=useUserStore.getRemberPassword()

 })
 //用户登录功能
 const login = () => {
    
    //前端验证
    if(user.value.account==''||user.value.password==''){
       ElMessage.error('账号或密码不能为空')
       return
    }
    //传入后端
    http.post('/user/login',user.value).then(res=>{
      if(res.code === 200){
        //保存账号密码  保存用户信息和token
        if(isRemember.value){
          useUserStore.remenberUser(user.value.account,user.value.password)
        }
        localStorage.setItem('token',res.data.token)
        useUserStore.user.id=res.data.id
        useUserStore.user.name=res.data.name
        useMenuStore.tags=[]
        //跳转到首页
       ElMessage.success("登录成功")
       router.replace('/main')
      }
    })}

</script>
<style scoped>

    #outer{
      position: absolute; top: 0px; bottom: 0px;
      width: 100%;
     background: url('https://cdn.pixabay.com/photo/2018/08/14/13/23/ocean-3605547_1280.jpg') no-repeat;
     background-size: cover;
    }
    #login_box {
      width: 400px;
      background-color: #00000060;
      margin: auto;
      margin-top: 10%;
      padding-bottom: 50px;
      padding-top: 30px;
      border-radius: 10px;
      
    }
    h2 {
      color: #ffffff90;
      text-align: center;
      margin-top: 5%;
    }
    #input_box{
        padding-left: 20%;
        padding-right: 20%;
    }
    .el-input{
        margin-top: 5%;
        
    }
    :deep(.el-input__wrapper){
     background-color:rgba(0,0,0,0);

    }
    :deep(.el-input__inner) {
    background-color: rgba(0, 0, 0, 0) !important;
    color: #fff;
    
    }
   #toregister{
    color: pink;
   }

 

    #sign_up {
      margin-top: 45%;
      margin-left: 60%;
    }

    a {
      color: #b94648;
    }
</style>