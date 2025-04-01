<template>
    <div style="height: 92%; padding-top: 40px;">
        <div class="addtype" @click="openEditDialog"><el-icon>
                <Plus />
            </el-icon>&nbsp;&nbsp;&nbsp;<span>添加下级</span></div>
        <!-- el-tree-v2 -->
        <el-tree-v2 style="max-width: 300px;margin-left: 10%;" :height="400" :data="typeShow" :props="{ label: 'name' }"
            class="tree" :indent="20" @node-contextmenu="openContextMenu">
            <template #default="{ node }">
                <span class="prefix" :class="{ 'is-leaf': node.isLeaf }"></span>

                <span v-if="node.data.children.length == 0"><el-icon>
                        <Document />
                    </el-icon></span>
                <span v-else><el-icon>
                        <Folder />
                    </el-icon></span>

                <span>{{ node.label }}</span>
            </template>
        </el-tree-v2>
        <!-- 增加和修改的弹窗 -->
        <el-dialog v-model="editDialog.visible" :title="editDialog.title" width="500">
            <div class="dialog-body">
                <div style="margin-top: 20px;">类别:<el-input v-model="editDialog.data.name" placeholder="请输入内容"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="editDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="addType" v-if="editDialog.addable">
                        提交
                    </el-button>
                    <el-button type="primary" @click="updateType" v-else>
                        修改
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <!-- 删除的弹窗 -->
        <el-dialog v-model="deleteDialog.visible" title="确认删除" width="500">
            <div class="dialog-body">
                {{ deleteDialog.content }}
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="deleteDialog.visible = false">关闭</el-button>
                    <el-button type="primary" @click="deleteType" v-if="deleteDialog.deletable">
                        删除
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <ul v-show="contextmenu.visible" :style="{ left: contextmenu.left + 'px', top: contextmenu.top + 'px' }"
            class="contextmenu" ref="menuRef">
            <li @click="openEditDialog(1)">修改</li>
            <li @click="openDeleteDialog(contextmenu.data)">删除</li>
            <li @click="openEditDialog()">添加下级</li>
        </ul>
    </div>

</template>
<script setup>

import { ref, onMounted } from 'vue'
import http from '../../untils/http';
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getEditDialog } from './typeMethod';
import { Folder, Document } from '@element-plus/icons-vue'
//  变量池
const typeShow = ref([])   //  用于页面显示材料信息
const contextmenu = ref({ visible: false, left: 0, top: 0 }) //  右键菜单
const deleteDialog = ref({visible:false})
const editDialog = getEditDialog()
//  初始化 
onMounted(() => {
    show()
})

//  增加和修改

const openEditDialog = (ch) => {
    if (ch === 1) {
        editDialog.toUpdate(contextmenu.value.data)
    } else {
        editDialog.toAdd(contextmenu.value.data)
    }
}
const addType = () => {
    http.post('/type/add', editDialog.data).then(res => {
        if (res.code == 200) {
            ElMessage.success('添加成功')
            editDialog.visible = false
            show()
        }
    })
}
const updateType = () => {
    // delete editDialog.data.children
    http.post('/type/update', editDialog.data).then(res => {
        if (res.code == 200) {
            ElMessage.success('修改成功')
            editDialog.visible = false
            show()
        }
    })
}

//  打开删除的对话框
const openDeleteDialog = (data) => {
    http.post('/type/deletePre', data,JSON).then(res => {
        if (res.code == 200) {
           deleteDialog.value.visible= true
           deleteDialog.value.content= res.data.resMsg
           deleteDialog.value.deletable=res.data.resFlag
           deleteDialog.value.data=data
        }
    })
}
const deleteType = () => {
    http.post('/type/delete', { id: deleteDialog.value.data.id }).then(res => {
        if (res.code == 200) {
            ElMessage.success('删除成功')
            deleteDialog.value={visible:false}
            show()
        }
    })
}

// 右键菜单相关
const openContextMenu = (e, data) => {//右键打开菜单
    contextmenu.value = { visible: true, top: e.clientY, left: e.clientX, data: data }
    document.body.addEventListener('click', closeContextmenu)
    document.body.addEventListener('contextmenu', closeContextmenu)
}
const closeContextmenu = () => {
    contextmenu.value = { visible: false, data: {} }
    document.body.removeEventListener('click', closeContextmenu)
    document.body.removeEventListener('contextmenu', closeContextmenu)
}
// 通用方法
const show = () => {
    http.post('/type/show').then(res => {
        if (res.code == 200) {
            typeShow.value = res.data
        }
    })
}
</script>
<style scoped>
.addtype {
    max-width: 300px;
    height: 26px;
    line-height: 26px;
    font-size: 14px;
    font-family: 'Microsoft YaHei';
    color: blue;
    margin-left: 10%;
    padding-left: 5px;
    cursor: pointer;
}

.addtype:hover {
    background-color: aqua;
}

.dialog-body {
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    margin-top: 20px;
    margin-bottom: 20px;
}

.contextmenu {
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: absolute;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
}

.contextmenu li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
}

.contextmenu li:hover {
    background: #eee;
}

/* .tree-line{

  .el-tree-node {
            
          
        }

} */
</style>