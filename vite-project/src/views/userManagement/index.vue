<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header">
                    <span>查询条件</span>
                </div>
            </template>
            <span>用户名称</span>
            <el-input v-model="searchName" style="width: 400px;margin-left: 30px;" clearable />
            <el-button type="primary" style="margin-left: 30px;" @click="search">查询</el-button>
        </el-card>
        <div style="padding-left:0px;padding-top: 30px;padding-bottom: 5px;">
            <el-button type="info" @click="openDialog('add', roleParm)" class="btn_add" :icon="Plus"
                style="margin-left: 0px;">添加用户</el-button>
        </div>
        <el-table :data="userShow" border style="width: 100%" max-height="380px">
            <el-table-column prop="account" label="账号" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="birthday" label="出生年月" />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" @click="openDialog('update', scope.row)"
                        style="color: skyblue;font-size: 14px;">
                        编辑
                    </el-button>
                    <el-button size="small" @click="openDialog('delete', scope.row)"
                        style="color: skyblue;font-size: 14px;">
                        删除
                    </el-button>
                    <el-button size="small" @click="openDialog('role', scope.row)"
                        style="color: skyblue;font-size: 14px;">
                        关联角色
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div style="margin-top: 5px;">
            <el-pagination size="small" background layout="prev, pager, next,->,total" :total="page.total"
                @current-change="pageChange" v-model:page-size="page.pageSize"
                v-model:current-page="page.currentPage" />
        </div>


        <!-- 新增用户的对话框 -->
        <el-dialog title="新增用户" v-model="addDialog.visible" width="40%">
            <div class="dialog-body">
                <el-form :model="userParm" label-width="30%" style="max-width: 600px">
                    <el-form-item label="用户名称:">
                        <el-input v-model="userParm.name" />
                    </el-form-item>
                    <el-form-item label="账号:">
                        <el-input v-model="userParm.account" />
                    </el-form-item>
                    <el-form-item label="出生年月:">
                        <el-date-picker type="date" placeholder="Pick a date" v-model="userParm.birthday"
                            value-format="YYYY-MM-DD" />
                    </el-form-item>
                </el-form>

            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="addUser">
                        新增
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 确认删除的对话框 -->
        <el-dialog title="操作提示" v-model="deleteDialog.visible" width="40%">
            <span>确定删除该用户？</span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="deleteDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="deleteUser">
                        删除
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 修改角色的对话框 -->
        <el-dialog title="修改用户" v-model="updateDialog.visible" width="40%">
            <div class="dialog-body">
                <el-form :model="userParm" label-width="30%" style="max-width: 600px">
                    <el-form-item label="用户名称:">
                        <el-input v-model="userEdit.name" />
                    </el-form-item>
                    <el-form-item label="账号:">
                        <el-input v-model="userEdit.account" />
                    </el-form-item>
                    <el-form-item label="出生年月:">
                        <el-date-picker type="date" placeholder="Pick a date" v-model="userEdit.birthday"
                            value-format="YYYY-MM-DD" />
                    </el-form-item>

                </el-form>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="updateDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="updateUser">
                        修改
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 关联角色的对话框 -->
        <el-dialog title="关联角色" v-model="roleRelation.visible" width="40%">
            <div class="dialog-body">
                <div class="m-4">
                    <span style="margin-right: 10px;">选择角色</span>
                    <el-cascader :options="roleList" :props="props" clearable  v-model="roleSelected"/>
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="roleRelation.visible = false">关闭</el-button>
                    <el-button type="primary" @click="subRole">
                        提交
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { userStore } from '../../store/user';
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import http from '../../untils/http';
import { ElMessage } from 'element-plus'
//变量池
const useUserStore = userStore()
const page = ref({ currentPage: 1, pageSize: 8, total: 0 })
const userParm = ref({ name: '', account: '', birthday: '' }) //角色参数
const userEdit = ref({})
const searchName = ref('') //搜索框
const userShow = ref([]) //角色列表-表格
const addDialog = ref({ visible: false })        //新增角色对话框是否存在
const deleteDialog = ref({ visible: false })    //确认删除对话框是否存在
const updateDialog = ref({ visible: false })    //修改角色对话框是否存在    
const roleRelation = ref({ visible: false })    //关联菜单对话框是否存在
const props = { multiple: true,label:'name',value:'id'}
const roleList= ref([])
const roleSelected = ref([])

// onMounted 初始化 获取数据
onMounted(() => {
    http.post('/user/show', page.value).then(res => {
        if (res.code == 200) {
            userShow.value = res.data.list
            page.value.total = res.data.count
        }
    })
})
// 打开对话框的设置
const openDialog = (str, data) => {
    if (str == 'add')
        addDialog.value.visible = true
    else if (str == 'delete')
        deleteDialog.value.visible = true
    else if (str == 'update'){
        updateDialog.value.visible = true
        userEdit.value = JSON.parse(JSON.stringify(data))
    }  
    else if (str == 'role') {
        roleRelation.value.visible = true
        roleSelected.value = []
        http.post('/role/getUserRelativeRole',data).then(res => {
            if (res.code == 200) {
                roleList.value=res.data.roleList
                res.data.userRole.forEach(item=>{
                    roleSelected.value.push(item.id)
                })
            }
        })
    }
    userParm.value = str == 'add' ? { name: '', account: '', birthday: '' } : data
}
// 添加角色
const addUser = () => {

    http.post('/user/add', userParm.value,JSON).then(res => {
        if (res.code == 200) {
            ElMessage.success('添加成功')
            addDialog.value.visible = false
            search()
        }
    })
}
// 删除角色
const deleteUser = () => {
    if (userParm.value.id == useUserStore.user.id) {
        ElMessage.error('没有删除自己的权限')
        deleteDialog.value.visible = false
        return
    }
    
    http.post('/user/delete', userParm.value).then(res => {
        if (res.code == 200) {
            console.log(res)
            ElMessage.success('删除成功')
            deleteDialog.value.visible = false
            show()
        }
    })
}
// 编辑角色
const updateUser = () => {

    if (userEdit.name == '' || userEdit.account == '') {
        ElMessage.warning('请输入完整信息')
        return
    }
    http.post('/user/update', userEdit.value).then(res => {
        if (res.code == 200) {
            ElMessage.success('编辑成功')
            updateDialog.value.visible = false
            show()
        }
    })
}
//查询功能
const search = () => {
    page.value.currentPage = 1
    show()
}

const subRole = () => { //给用户关联角色
    
    const param = {
        id: userParm.value.id,
        idList: roleSelected.value.map(item => item[0])
    }
    http.post('/user/giveUserRole',param,JSON).then(res => {
        roleRelation.value.visible = false
        show()
        
    })
}

//分页
const pageChange = (val) => {
    show()
}
//通用方法 --》 show()
const show = () => {
    http.post('/user/show', {
        name: searchName.value,
        currentPage: page.value.currentPage,
        pageSize: page.value.pageSize
    }).then(res => {
        if (res.code == 200) {
            userShow.value = res.data.list
            page.value.total = res.data.count
        }
    })
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

.dialog-body {
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    margin-top: 20px;
    margin-bottom: 20px;
    padding-left: 10%;
    padding-right: 10%;
}
</style>