<template>
    <div style=" padding-top: 40px;">
        <div class="addmenu" @click="showDialog(null)"><el-icon>
                <Plus />
            </el-icon>&nbsp;&nbsp;&nbsp;<span>添加一级菜单</span></div>
        <el-tree-v2 style="max-width: 300px;margin-left: 10%;" :height="400" :data="showData" :props="{label:'text'}"
            @node-contextmenu="handleClick" >
        <template #default="{ node }">
            <span class="prefix" :class="{ 'is-leaf': node.isLeaf }"></span>
            <span>{{ node.label }}</span>
        </template>
        </el-tree-v2>
        <el-dialog v-model="dialogMsg.visible" :title="dialogMsg.title" width="500">
            <div class="dialog-body">
                <div style="margin-top: 20px;">菜单名称:<el-input v-model="newMenu.text" placeholder="请输入内容"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
                <div style="margin-top: 10px;">菜单链接:<el-input v-model="newMenu.url" placeholder="请输入内容"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
                <div style="margin-top: 10px;">菜单图标:<el-input v-model="newMenu.icon" placeholder="请输入内容"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
                <div style="margin-top: 10px;;">菜单顺序:<el-input v-model="newMenu.order" placeholder="请输入内容"
                        style="width: 250px;margin-left: 35px;"></el-input></div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogMsg.visible=false">关闭</el-button>
                    <el-button type="primary" @click="addNewMenu">
                        {{dialogMsg.btnName}}
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <ul v-show="contextmenu.isVisible" :style="{left:contextmenu.left+'px',top:contextmenu.top+'px'}" class="contextmenu" ref="menuRef">
      <li @click="showDialog(contextmenu.data,1)">修改</li>
      <li @click="deleteMenu(contextmenu.data)">删除</li>
      <li @click="showDialog(contextmenu.data)">添加下级</li>
    </ul>


    </div>

</template>
<script setup>
import { ref, onMounted,onBeforeUnmount } from 'vue'
import http from '../../untils/http';
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { isVisible } from 'element-plus/es/utils/index.mjs';
//  变量池
const dialogMsg = ref({ visible: false,title: '', content: '',btnName: '新增' }) //弹窗内容
const showData = ref([])    //表格数据
const queryData = ref(null)     //查询数据
const newMenu = ref({ text: '', icon: '', url: '', order: '' })  //新增菜单
const contextmenu=ref({isVisible:false,top:0,left:0,data:{}})  //右键菜单
const menuRef = ref(null)
//    初始化
onMounted(() => {
    http.post('/menu/getAll',).then(res => {
        if (res.code == 200) {
            queryData.value = res.data
            menuConvert(queryData.value)
        }
    })
})
onBeforeUnmount(()=>{
    document.body.removeEventListener('click',show)
    document.body.removeEventListener('contextmenu',show)
})

//  打开对话框
const showDialog = (data,ch) => {
    newMenu.value = { text: '', icon: '', url: '', order: '' }
    // 判断是修改还是添加
    if(ch){// 修改
        dialogMsg.value.btnName='修改'
        dialogMsg.value.title = '“'+data.text+'”的修改界面'
        newMenu.value = JSON.parse(JSON.stringify(data)) 
        delete newMenu.value.children
        
    } 
    else { // 新增
        if(data==null)
            dialogMsg.value.title = '添加一级菜单'
        else{
            if(data.pid!=null){
                ElMessage.error('暂不支持三级菜单')
                return
            }
            newMenu.value.pid = data.id
            newMenu.value.url = "/main/"
            dialogMsg.value.title = '新增“'+data.text+'”的二级菜单'  
        }
        dialogMsg.value.btnName='新增'  
    }
    dialogMsg.value.visible = true
}
// 添加菜单 执行添加
const addNewMenu = () => {
    // 前端验证
    if (newMenu.value.text == '' || newMenu.value.icon == '' || newMenu.value.order == '') {
        ElMessage.error('数据不完整')
        return
    }
    let url=dialogMsg.value.btnName=='新增'?'/menu/add':'/menu/update'
    console.log(url,newMenu.value)
    http.post(url, newMenu.value).then(res => {
        if (res.code == 200) {
            ElMessage.success(dialogMsg.value.btnName+'成功')
            dialogMsg.value.visible = false
            // 重新加载菜单
            queryData.value = res.data
            menuConvert(queryData.value)
        }
    })
}
const deleteMenu=(data)=>{
    http.post('/menu/delete',data).then(res=>{
        if(res.code==200){
            ElMessage.success('删除成功')
            // 重新加载菜单
            queryData.value = res.data
            menuConvert(queryData.value)
        }
    })
}
// 菜单转换
const menuConvert = (dataParam) => {
    showData.value = []
    dataParam.forEach(element => {
        //const node = element
        if (element.pid == null) {
            showData.value.push(element)
        } else {
            const parent = showData.value.find(item => item.id == element.pid)
            if (parent) {
                if (parent.children) {
                    parent.children.push(element)
                } else {
                    parent.children = [element]
                }
            }
        }
    });
    //重新赋值
    showData.value = JSON.parse(JSON.stringify(showData.value))

}
//右键打开菜单
const handleClick = (event, data, node) => {
    contextmenu.value.isVisible = true
    contextmenu.value.top = event.clientY
    contextmenu.value.left = event.clientX
    contextmenu.value.data = data
    document.body.addEventListener('click',show) //点击空白处关闭菜单
    document.body.addEventListener('contextmenu',show)//右键关闭菜单
}
const closeContextmenu = () => {
    contextmenu.value.isVisible = false
    // console.log('全局事件监听点击->关闭右键菜单')
    document.body.removeEventListener('click',show)
    document.body.removeEventListener('contextmenu',show)
}
const show = (event) => {
    // if(!menuRef.value.contains(event.target))
         closeContextmenu()    
}


</script>
<style scoped>
.addmenu {
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

.addmenu:hover {
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


</style>