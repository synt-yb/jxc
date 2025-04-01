
<script setup>
    import {ref,onMounted, watch} from 'vue'
import Header from '../components/header.vue';
    let readonly=ref(false);
    let user=ref({});
   
    const userList = ref([])
    let userListSize=ref();
    onMounted(()=>{
      userList.value=[
        {id:1,name:'张三',age:18,createTime:'2023-03-01 12:00:00'},
        {id:2,name:'李四',age:19,createTime:'2023-03-02 12:00:00'},
        {id:3,name:'王五',age:20,createTime:'2023-03-03 12:00:00'},
      ]
      userListSize.value=userList.value.length
    })
    function addUser(){
       
       if(user.value.id==null||user.value.name==null||user.value.age==null||user.value.age>100)
       alert('未完整输入或数据有误')
       else{
        if(readonly.value){
            let index=0;
            for(let i=0;i<userList.value.length;i++){
                if(user.value.id==userList.value[i].id){
                   index=i;
                    break;
                }
            }
            console.log(user.value)
            userList.value[index]=user.value;
            readonly.value=false;
        }else{
            for(let i=0;i<userList.value.length;i++){
                if(user.value.id==userList.value[i].id){
                   alert('id重复')
                    return;
                }
            }
            user.value.createTime=dateFormat(new Date())
           userList.value.push(user.value) 
        }
        user.value={}
        }
    }
    function delUser(item){
        userList.value.splice(item,1)
    }
    function editUser(id,name,age,createTime){
       user.value.id=id
       user.value.name=name
       user.value.age=age
       user.value.createTime=createTime
       readonly.value=!readonly.value;
    }
    function dateFormat(date){
        let year=date.getFullYear()
        let month=(date.getMonth()+1).toString().padStart(2,'0')
        let day=date.getDate().toString().padStart(2,'0')
        let hour=date.getHours().toString().padStart(2,'0')
        let min=date.getMinutes().toString().padStart(2,'0')
        let sec=date.getSeconds().toString().padStart(2,'0')
        return year+'-'+month+'-'+day+' '+hour+':'+min+':'+sec
    }
    // watch(userList,()=>{
    //     userListSize.value=userList.value.length
    // },{
    //     deep:true
    // })
    watch(()=>userList.value.length,(newValue)=>{
        userListSize.value=newValue
    })
    let headerVal=ref({})
    const DateGet=(a,b)=>{
        console.log(a)
        headerVal.value.name=a
        console.log(b)
    }
    let headerRef=ref()
    const doSomeThing=()=>{
        headerRef.value.changeBackground()
    }
    import {
 Grid
} from '@element-plus/icons-vue'
</script>
<template>
    <Header ref="headerRef" name="register" :val="headerVal" @send="DateGet"><el-button type="info" round>Info</el-button>
       <template #header><el-button type="success" round>Info</el-button></template> 
       <template #footer><el-button type="danger" round>Info</el-button></template>
    </Header>
    <input type="text" v-model="headerVal.name">
    
    <el-button type="success" :icon="Grid" plain @click="doSomeThing"></el-button>
    <div id="userManagement">
        <h2 style="text-align: center;">用户管理</h2>
        <table style="width: 600px; margin: 0 auto;background-color: #add8e6;" cellspacing="0" cellpadding="0">
            <tbody style="height: 50px;" >
              <tr style="height: 40px;">
                <td colspan="5" style="text-align: center;">
                编号:<input type="text" v-model="user.id" v-if="readonly" readonly></input>
                <input type="text" v-model="user.id" v-if="!readonly" ></input>
                姓名:<input type="text" v-model="user.name"></input>
                年龄:<input type="number" v-model="user.age" ></input>
                <input type="text" v-model="user.createTime" v-show="false">
                &nbsp;<button style="background-color: orange; color: white; border: orange solid 1px;" @click="addUser()">提交</button>
              </td>
            </tr>
            <tr style="height: 50px;">
                <th>编号</th>
                <th>姓名</th>
                <th>年龄</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </tbody>
            <tbody v-for="(item,index) in userList" :key="item.index">
            <tr id="mytr">
                <td >{{item.id}}</td>
                <td >{{item.name}}</td>
                <td >{{item.age}}</td>
                <td >{{item.createTime}}</td>
                <td >
                    <a href="#" @click.prevent="editUser(item.id,item.name,item.age,item.createTime)">修改</a>|<a href="#" @click.prevent="delUser(item)">删除</a>
                </td>
            </tr>
            </tbody>
            <tbody v-if="userListSize!=0">
                <tr><td colspan="5">用户数量:{{ userListSize }}</td></tr>
            </tbody>
        </table>
    </div>
</template>
<style scoped>
    #userManagement input{
        width: 100px;   
    }
    #userManagement td{
       text-align: center;
      
    }
    #userManagement th{
       border-top: white dotted 1px;
    }
   

    #mytr td{
        border: blue dotted 1px;
        margin: 0px; 
        background-color: #fff;
        height: 35px;
    }
</style>