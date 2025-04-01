<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header">
                    <span>查询条件</span>
                </div>
            </template>
            <span>角色名称</span>
            <el-input v-model="searchName" style="width: 400px;margin-left: 30px;" clearable/>
            <el-button type="primary" style="margin-left: 30px;" @click="search">查询</el-button>
        </el-card>
        <div style="padding-left:0px;padding-top: 30px;padding-bottom: 5px;">
            <el-button type="info" @click="openDialog('add', roleParm)" class="btn_add" :icon="Plus"
                style="margin-left: 0px;">添加角色</el-button>
        </div>
        <el-table :data="roleShow" border style="width: 100%" height="400px">
            <el-table-column prop="name" label="角色" />
            <el-table-column prop="flag" label="标识" />
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
                    <el-button size="small" @click="openDialog('menu', scope.row)"
                        style="color: skyblue;font-size: 14px;">
                        关联菜单
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <span>{{ totalShow }}</span>
        <!-- 新增角色的对话框 -->
        <el-dialog title="新增角色" v-model="addDialog.visible" width="40%">
            <div class="dialog-body">
                <div style="margin-top: 20px;">角色名称:<el-input placeholder="请输入内容" v-model="roleParm.name"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
                <div style="margin-top: 10px;">角色标识:<el-input placeholder="请输入内容" v-model="roleParm.flag"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="addRole">
                        新增
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 确认删除的对话框 -->
        <el-dialog title="操作提示" v-model="deleteDialog.visible" width="40%">
            <span>确定删除该角色？</span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="deleteDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="deleteRole">
                        删除
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 修改角色的对话框 -->
        <el-dialog title="编辑角色" v-model="updateDialog.visible" width="40%">
            <div class="dialog-body">
                <div style="margin-top: 20px;">角色名称:<el-input placeholder="请输入内容" v-model="roleEdit.name"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
                <div style="margin-top: 10px;">角色标识:<el-input placeholder="请输入内容" v-model="roleEdit.flag"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="updateDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="editRole">
                        修改
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 关联菜单的对话框 -->
        <el-dialog title="关联菜单" v-model="menuRelation.visible" width="40%">
            <div class="dialog-body">
                <el-tree-v2 style="max-width: 300px;margin-left: 10%;" :height="400" :data="menuData"
                    :props="{ label: 'text' }" show-checkbox :default-checked-keys="checkedKeys" ref="treeRef"
                    @check="check">
                    <template #default="{ node }">
                        <span class="prefix" :class="{ 'is-leaf': node.isLeaf }"></span>
                        <span>{{ node.label }}</span>
                    </template>
                </el-tree-v2>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="menuRelation.visible = false">关闭</el-button>
                    <el-button type="primary" @click="submitMenu">
                        提交
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, onMounted,watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import http from '../../untils/http';
import { ElMessage } from 'element-plus'
import { menuConvert, getKeys } from '../../untils/menu'
//变量池
const roleParm = ref({ name: '', flag: '' }) //角色参数
const roleEdit = ref({})
const searchName = ref('') //搜索框
const roleShow = ref([]) //角色列表-表格
const totalShow = ref('')
const addDialog = ref({ visible: false })        //新增角色对话框是否存在
const deleteDialog = ref({ visible: false })    //确认删除对话框是否存在
const updateDialog = ref({ visible: false })    //修改角色对话框是否存在    
const menuRelation = ref({ visible: false })    //关联菜单对话框是否存在
const menuData = ref([])
const checkedKeys = ref([])
const checkedPakeys = ref([])
const treeRef = ref(null)
//
const search = () => {
    http.post('/role/select', { name: searchName.value }).then(res => {
        if (res.code == 200) {
            roleShow.value = res.data
        }
    })

}
// 获取角色列表
onMounted(() => {
    http.post('/role/show').then(res => {
        if (res.code == 200) {
            roleShow.value = res.data
        }
    })
})
// dialog框相关
const openDialog = (str, data) => {
    if (str == 'add')
        addDialog.value.visible = true
    else if (str == 'delete')
        deleteDialog.value.visible = true
    else if (str == 'update'){
        updateDialog.value.visible = true
        roleEdit.value = JSON.parse(JSON.stringify(data))
    }
    else if (str == 'menu') {
        menuRelation.value.visible = true
        roleParm.value = data
        http.post('/menu/getRelativeMenu', roleParm.value).then(res => {

            if (res.code == 200) {
                menuData.value = menuConvert(res.data.allMenu)

                // 清空结点
                treeRef.value.setCheckedKeys([])
                checkedKeys.value = getKeys(res.data.roleList)


            }
        })
    }
    roleParm.value = str == 'add' ? { name: '', flag: '' } : data
}
// 添加角色
const addRole = () => {
    if (roleParm.name == '' || roleParm.flag == '') {
        ElMessage.warning('请输入完整信息')
        return
    }
    http.post('/role/add', roleParm.value).then(res => {
        if (res.code == 200) {
            ElMessage.success('添加成功')
            addDialog.value.visible = false
            roleShow.value = res.data
        }
    })
}
// 删除角色
const deleteRole = () => {
    http.post('/role/delete', roleParm.value).then(res => {
        if (res.code == 200) {
            ElMessage.success('删除成功')
            roleShow.value = res.data
            deleteDialog.value.visible = false
            deleteDialog.value.data = {}
        }
    })
}
// 编辑角色
const editRole = () => {

    if (roleParm.name == '' || roleParm.flag == '') {
        ElMessage.warning('请输入完整信息')
        return
    }
    http.post('/role/update', roleEdit.value).then(res => {
        if (res.code == 200) {
            ElMessage.success('编辑成功')
            updateDialog.value.visible = false
            roleShow.value = res.data
        }
    })
}
// 提交关联菜单
const check = (data, node) => {
    checkedKeys.value=node.checkedKeys
    checkedPakeys.value=node.halfCheckedKeys
}
const submitMenu = () => {
    
    
    checkedPakeys.value.push(...checkedKeys.value)
    
    
    const param = {
        id: roleParm.value.id,
        idList: checkedPakeys.value
    }
    console.log(param)
    http.post('/menu/updateRoleMenu',param,JSON).then(res => {
        if (res.code == 200) {
            ElMessage.success('提交成功')
            menuRelation.value.visible = false
        }
    })

}
//监视RoleShow的变化
watch(roleShow, (newVal, oldVal) => {
    if (newVal.length == 0) {
        totalShow.value = ''
    } else {
        totalShow.value = '显示第1条到'+newVal.length+'条记录,共'+roleShow.value.length+'条记录'
    }
})
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
}
</style>