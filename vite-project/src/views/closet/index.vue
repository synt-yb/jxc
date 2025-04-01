<template>
  <el-card >
    <template #header>
      <div class="card-header">
        <span>按条件查询</span>
      </div>
    </template>
    <el-row :gutter="20">
    <el-col :span="5">姓名：<el-input aria-placeholder="输入姓名" style="width: 200px;" v-model="selectMsg.name"></el-input></el-col>
    <el-col :span="4">性别：<el-input aria-placeholder="输入姓名" style="width: 100px;" v-model="selectMsg.sex"></el-input></el-col>
    <el-col :span="5">出生日期：<el-input aria-placeholder="输入姓名" style="width: 150px;" v-model="selectMsg.date"></el-input></el-col>
    <el-col :span="6">地址：<el-input aria-placeholder="输入姓名" style="width: 200px;" v-model="selectMsg.address"></el-input></el-col>
    <el-col :span="4"><el-button type="primary" @click="select">查询</el-button><el-button type="primary" @click="isOpenDialog">新增</el-button></el-col>

  </el-row >

    
  </el-card>
    <el-table :data="tableData" style="width: 100%" stripe @selection-change="handleSelectionChange" border >
    <el-table-column type="selection" fixed  width="55" />
    <el-table-column prop="id" label="序号" width="150" />
    <el-table-column prop="name" label="姓名" width="150" />
    <el-table-column prop="sex" label="性别" width="150" />
    <el-table-column prop="date" label="出生日期" width="150" />
    <el-table-column prop="address" label="地址" show-overflow-tooltip width="250" />
    <el-table-column  label="操作" fixed="right" >
        <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.row)">
          编辑
        </el-button>
        <el-button
          size="small"
          type="danger"
          @click="handleDelete(scope.row)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination 
    layout="sizes,prev, pager, next, -> ,jumper, total" 
    :total="50" 
    :page-sizes="[10, 20, 100, 200]"
    v-model:current-page="querytable.currentPage"
    v-model:page-size="querytable.pageSize"
    @change="tableDataInit"
  />
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="500"
  >
  <el-form ref="ruleFormRef" :model="formMsg" label-width="auto" style="max-width: 600px" :rules="rules">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="formMsg.name" />
    </el-form-item>
  </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addsubmit">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
    
import {ref,onMounted,} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { da } from 'element-plus/es/locales.mjs';
const tableData = ref([])
const querytable=ref({currentPage:1,pageSize:10}) 
let dialogVisible = ref(false)
let dialogTitle=ref('新增')
const isOpenDialog=()=>{
    dialogVisible.value=true
    dialogTitle.value='新增'
}
const formMsg=ref({})
const ruleFormRef = ref({})
import {debounce,throttle} from 'lodash'
const addsubmit=throttle(()=>{
  ruleFormRef.value.validate((valid, fields) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!', fields)
    }
  })
},1000)
const tableDataInit=()=>{
    tableData.value=[
        {id:'1',name:'王小虎',sex:'男',date:'2016-5-7,',address:'上海市普陀区金沙江路 1518 弄'},
        {id:'2',name:'王小龙',sex:'男',date:'2016-5-7',address:'上海市普陀区金沙江路 1517 弄'},
        {id:'3',name:'王小凤',sex:'女',date:'2016-5-7',address:'上海市普陀区金沙江路1519 弄'},
        {id:'4',name:'王小菊',sex:'女',date:'2016-5-7',address:'上海市普陀区金沙江路1519 弄'},
    ]
   console.log(querytable.value)
}
onMounted(()=>{
    tableDataInit()
})
const handleSelectionChange=(row)=>{
    console.log(row)
}
const selectMsg=ref({})
const select=()=>{
    querytable.value.currentPage=1
    console.log(selectMsg.value)
    
}

const rules = ref({
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 3, max: 5, message: '姓名长度在3-5', trigger: 'blur' },
  ]
})
const handleEdit=(date)=>{
  let dateTemp=JSON.stringify(date)
  formMsg.value=JSON.parse(dateTemp)
  dialogVisible.value=true
  dialogTitle.value='编辑'
}
const handleDelete=(date)=>{
  ElMessageBox.confirm(
    '您确定要删除该条数据信息吗?',
    '确认删除',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
     console.log(date.id)
    })
    .catch(() => {})
}
</script>
<style scoped>
 :deep(.el-card__header){
  background-color: #409EFF;
  color: white;
 }
</style>