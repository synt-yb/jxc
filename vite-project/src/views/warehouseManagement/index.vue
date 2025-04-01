<template>
    <div class="" style="margin-bottom: 30px;padding-bottom: 70px;">
      <div class="search_card">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header" style="width: 70px;">
                    <span>查询条件</span>
                </div>
            </template>
            <div style="text-align: center;">
                <span>仓库名称</span>
                <el-input v-model="searchName" style="width: 400px;margin-left: 30px;" clearable />
                <el-button type="primary" style="margin-left: 30px;" @click="showWarehouse()">查询</el-button>
            </div>
        </el-card>
      </div>
      
        <div class="btn_list">
            <el-button type="primary" style="margin-left: 0px;" @click="openAddNewWarhouseDialog">新增仓库</el-button>
            <el-button type="primary" @click="collapseAll">一键收起</el-button>
        </div>
        <div class="warehouse_show" >
      <el-collapse v-model="activeNames" >
        <el-collapse-item  :name="item.id" style="border: 1px solid black;margin-bottom: 5px;" v-for="item in warehouseShow">
          <template #title>
            <span style="font-size: 15px;margin-left: 30px;">{{item.warehouse_name}}</span>
        </template>
        <div style="margin-left: 40px;">
          <div>
           负责人:{{ item.manager_id }}
          </div>
          <div>
           地址:{{ item.location }}
          </div>
          <div>
           仓库容量:{{ item.capacity }}
          </div>
          <div>
           创建时间:{{ item.create_time }}
          </div>
        </div>
        
          
        </el-collapse-item>


     
     
      </el-collapse>
    </div>
    </div>
    <!-- /*新增仓库模态框 */ -->
    <el-dialog
    v-model="addNewWarehouseDialog"
    title="新增仓库"
    width="800"
    draggable
    overflow
  >
    <span>
      <el-form :model="NewMarehouseMsg" label-width="auto" style="max-width: 600px;margin: auto;">
    <el-form-item label="仓库名称">
      <el-input v-model="NewMarehouseMsg.warehouse_name" />
    </el-form-item>
    <el-form-item label="仓库地址">
      <el-input v-model="NewMarehouseMsg.location" />
    </el-form-item>
    <el-form-item label="管理员">
      
      <el-select v-model="NewMarehouseMsg.manager_id" placeholder="请选择仓库管理员" style="width: 240px;" clearable>
     
        <el-option v-for="item in warehouseManagerList" 
        :key="item.id"
        :label="item.name" 
        :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="仓库容量">
      <el-input v-model="NewMarehouseMsg.capacity" style="width: 240px;"/><span>&nbsp;&nbsp;&nbsp;平方米</span>
    </el-form-item>
    <el-form-item label="创建时间">
     
      <el-date-picker
          v-model="NewMarehouseMsg.create_time"
          type="date"
          placeholder="Pick a date"
          style="width: 240px"
        />
    </el-form-item>

  </el-form>
    </span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeAddNewMarehouse">关闭</el-button>
        <el-button type="primary" @click="addNewWarhouse">
          新增
        </el-button>
      </div>
    </template>
  </el-dialog>
  </template>
<script setup>
    import {ref,onMounted} from 'vue'
    import http from '../../untils/http.js';
    import { ElMessage } from 'element-plus'
    const activeNames=ref([])
    const warehouseShow=ref({})
    const searchName=ref('')
    const addNewWarehouseDialog=ref(false)
    const NewMarehouseMsg=ref([])
    const warehouseManagerList=ref({warehouse_name:'',location:'',capacity:'',manager_id:'',create_time:''})
    onMounted(()=>{
      showWarehouse()
    })
    //新增仓库功能
    const openAddNewWarhouseDialog=()=>{
      NewMarehouseMsg.value={}
      http.post('/warehouse/getWarehouseManagerList').then(res=>{
        warehouseManagerList.value=res.data.warehouseManagerList
      })
      addNewWarehouseDialog.value=true;
    }
    const closeAddNewMarehouse=()=>{
      addNewWarehouseDialog.value=false;
    }
    const addNewWarhouse=()=>{
     if(NewMarehouseMsg.value.warehouse_name!==null&&NewMarehouseMsg.value.warehouse_name!==undefined
      &&NewMarehouseMsg.value.location!==null&&NewMarehouseMsg.value.location!==undefined
      &&NewMarehouseMsg.value.capacity!==null&&NewMarehouseMsg.value.capacity!==undefined
      &&NewMarehouseMsg.value.manager_id!==null&&NewMarehouseMsg.value.manager_id!==undefined
      &&NewMarehouseMsg.value.create_time!==null&&NewMarehouseMsg.value.create_time!==undefined
     ){
        http.post('/warehouse/add',NewMarehouseMsg.value).then(res=>{
          if(res.code==200){
            ElMessage.success("添加成功")
            addNewWarehouseDialog.value=false
            showWarehouse()
          }
        })
     }else{
      ElMessage.error("信息不完整")
     }
    }
    const showWarehouse=()=>{
      
      const param={
        warehouse_name:searchName.value
      }
      
      http.post('/warehouse/show',param).then(res=>{
        if(res.code==200){
          warehouseShow.value=res.data.warehouseList
        }
      })
    }
    //一键收起
    const collapseAll=()=>{
      activeNames.value=[]
    }
</script>
<style scoped>

:deep(.el-card__header) {
    height: 40px;
    line-height: 40px;
    padding-top: 0px;
    background-color: #ECF5FF;
    font-size: 15px;
}
.search_card{
  margin-top: 20px;
  width: 80%;
  padding-left: 10%;
}
.btn_list{
    margin-top: 40px;
    width: 80%;
    padding-left: 10%;
}
.warehouse_show{
    width: 80%;
    padding-left: 10%;
    margin-top: 20px;
}
</style>