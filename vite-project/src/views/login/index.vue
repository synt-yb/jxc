<template>
  
    <div id="outer">
   1 <div id="login_box">
    <h2>LOGIN</h2>
    <div id="input_box">
      <input type="text" placeholder="请输入用户名" v-model="username">
    </div>
    <div class="input_box">
      <input type="password" v-model="password" placeholder="请输入密码">
    </div>
    <button @click="login">登录</button><br>
    <button @click="closet">衣柜</button><br>
  </div>
  </div>
</template>
<script setup>
 import { ref } from 'vue';
import { useRoute,useRouter } from 'vue-router';
import { indexStore } from '../../store/index.js';
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../../utils/http.js'
const aaStore=indexStore()
 const router = useRouter();
 let username=ref('')
 let password=ref('')
 const login = () => {
  http.post('/login',{
    username:username.value,
    password:password.value
  }).then(res=>{
    console.log(res)
    if(res.code==200){
     localStorage.setItem('token','111')
      router.replace('/main')
    }

  })
 }
 const closet=()=>{
  router.replace('/closet')
 }
</script>
<style scoped>

    #outer{
      position: absolute; top: 0px; bottom: 0px;
      width: 100%;
     background: url('https://cdn.pixabay.com/photo/2018/08/14/13/23/ocean-3605547_1280.jpg') no-repeat;
     background-size: cover;
    }
  #login_box {
      width: 20%;
      height: 300px;
      background-color: #00000060;
      margin: auto;
      margin-top: 10%;
      text-align: center;
      border-radius: 10px;
      padding: 50px 50px;
    }

    h2 {
      color: #ffffff90;
      margin-top: 5%;
    }

    #input-box {
      margin-top: 5%;
    }

    span {
      color: #fff;
    }

    input {
      border: 0;
      width: 60%;
      font-size: 15px;
      color: #fff;
      background: transparent;
      border-bottom: 2px solid #fff;
      padding: 5px 10px;
      outline: none;
      margin-top: 10px;
    }

    button {
      margin-top: 50px;
      width: 60%;
      height: 30px;
      border-radius: 10px;
      border: 0;
      color: #fff;
      text-align: center;
      line-height: 30px;
      font-size: 15px;
      background-image: linear-gradient(to right, #30cfd0, #330867);
    }

    #sign_up {
      margin-top: 45%;
      margin-left: 60%;
    }

    a {
      color: #b94648;
    }
</style>