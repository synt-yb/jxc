<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header">
                    <span>查询条件</span>
                </div>
            </template>
            <div style="text-align: center;">
                <span>产品名称</span>
                <el-input v-model="searchName" style="width: 400px;margin-left: 30px;" clearable />
                <el-button type="primary" style="margin-left: 30px;" @click="search">查询</el-button>
            </div>
        </el-card>
        <div style="height: 50px;"></div>
        <el-table :data="orderShow" border style="width: 100%" height="400px">
            <el-table-column type="index" width="30" />
            <el-table-column prop="type_name" label="产品" width="180" />
            <el-table-column prop="number" label="数量" />
            <el-table-column prop="price" label="单价" />
            <el-table-column prop="create_date" label="申请时间" width="200" sortable />
            <el-table-column label="状态">
                <template #default="scope">
                    <span v-if="scope.row.status == 1">在途</span>
                    <span v-if="scope.row.status == 2">驳回</span>
                    <span v-if="scope.row.status == 3">已通过</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="230">
                <template #default="scope">
                    <span style="margin-right: 10px;" v-if="scope.row.status != 3"><el-text class="btn-label"
                            type="primary" @click="revocation(scope.row.id)">撤回</el-text></span>
                    <span style="margin-right: 10px;" v-if="scope.row.status == 2"><el-text class="btn-label"
                            type="primary" @click="openDialog(scope.row)">修改</el-text></span>
                    <el-tooltip class="box-item" effect="dark" :content="scope.row.reason" placement="top"
                        v-if="scope.row.status == 2">
                        <span style="margin-right: 10px;"><el-text class="btn-label"
                                type="primary">查看原因</el-text></span>
                    </el-tooltip>
                    <span style="margin-right: 10px;" v-if="scope.row.status == 3"><el-text class="btn-label"
                            type="info">无操作</el-text></span>
                </template>
            </el-table-column>
        </el-table>
        <span>{{ totalShow }}</span>
        <el-dialog :title="dialog.title" v-model="dialog.visible" width="40%">
            <div class="dialog-body">
              
                <div style="margin-top: 10px;"><span class="label">单价</span><el-input
                    v-model="dialogMsg.price"     placeholder="请输入单价" type="number" class="other-input" />
                </div>
                <div style="margin-top: 10px;"><span class="label">数量</span><el-input
                    v-model="dialogMsg.number"    placeholder="请输入数量" type="number" class="other-input" />
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialog.visible = false">取消</el-button>
                    <el-button type="primary" @click="submit" >
                        提交审核
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus';
import { userStore } from '../../store/user'
import http from '../../untils/http.js';

//变量池
const dialog = ref({visible:false})
const dialogMsg= ref({})
const useUserStore = userStore()
const searchName = ref('') //搜索框
const orderShow = ref([])
const totalShow = ref('')
const search = () => {
    const params = {
        type_name: searchName.value,
        userId: useUserStore.user.id,
        order_type:2
    }
    console.log(params)
    http.post('/order/getOrderByUser',params).then(res => {
        if (res.code == 200) {
            orderShow.value = res.data
        }
    })
}
onMounted(() => {
    show()
})
const revocation = (data) => {
    http.post('/order/revocation', { id: data }).then(res => {
        if (res.code == 200) {
            ElMessage.success("撤销成功")
            show()
        }
    })
}
const show = () => {
    http.post('/order/getOrderByUser', {userId:useUserStore.user.id,order_type:2}).then(res => {
        if (res.code == 200) {
            orderShow.value = res.data
            
        }
    })
}
//
const openDialog=(data)=>{
    dialog.value.visible=true
    dialog.value.title=data.type_name
    dialogMsg.value=JSON.parse(JSON.stringify(data))
    console.log(data)
}
const submit = () => {
    dialogMsg.value.status=1
    http.post('/order/update', dialogMsg.value).then(res => {
        if (res.code == 200) {
            ElMessage.success("修改成功")
            dialog.value.visible = false
            show()
        }
    })
}
watch(()=>orderShow,(newValue)=>{
    let length=newValue.value.length
    if(length==0)
        totalShow.value = ''
    else
        totalShow.value = '显示第1条到'+length+'条记录,共'+length+'条记录'
},{deep:true})
</script>
<style scoped>
:deep(.el-card__header) {
    height: 40px;
    line-height: 40px;
    padding-top: 0px;
    background-color: #ECF5FF;
    font-size: 15px;
}

.dialog-body {
    padding-left: 15%;
    padding-right: 15%;
    font-size: 16px;
    font-weight: bold;
    margin-top: 20px;
    margin-bottom: 20px;

}

.dialog-body .label {
    display: inline-block;
    width: 20%;
    margin-right: 5%;
}

.dialog-body .type-input {
    width: 40%;
    margin-right: 5%;
}

.dialog-body .type-input {
    width: 40%;
    margin-right: 5%;
}

.dialog-body .other-input {
    width: 70%;
}

.btn-label {
    cursor: pointer;
}

.btn-label::selection {
    background: transparent;
    color: inherit;
}
</style>