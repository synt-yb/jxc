<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;padding-bottom: 50px;">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header">
                    <span>查询条件</span>
                </div>
            </template>
            <div style="text-align: center;">
                <span>物料名称</span>
                <el-input v-model="searchMsg.material_name" style="width: 400px;margin-left: 30px;" clearable />
                <el-button type="primary" style="margin-left: 30px;" @click="search()">查询</el-button>
            </div>

        </el-card>
        <div style="padding-top: 30px;padding-bottom: 5px;">
            <el-select v-model="searchMsg.category_id" placeholder="物料类型" style="width: 120px" @change="search()">
                <el-option label="全部类型" value="" />
                <el-option v-for="item in categoryShow" :label="item.name" :value="item.id" />

            </el-select>
            <el-button type="primary" style="margin-left: 30px;background-color: #ffa500;"
                @click="openDialogOfAddCategory">添加类型</el-button>
            <el-button type="primary" style="margin-left: 30px;background-color: #ffa500;"
                @click="openDialogOfAddMaterial">添加物料</el-button>
        </div>
        <el-table :data="materialShow" border style="width: 100%" stripe>

            <el-table-column prop="material_name" label="材料名称" width="200" show-overflow-tooltip fixed />
            <el-table-column prop="category_name" label="类别" width="120" />
            <el-table-column prop="shelf_life" label="保质期/天" width="100" />
            <el-table-column prop="min_stock" label="最低库存/m³" width="120" />
            <el-table-column prop="box_volume" label="每箱体积" width="100" />
            <el-table-column prop="box_quantity" label="每箱件数" width="100" />
            <el-table-column prop="unit" label="计量单位" width="100" />
            <el-table-column prop="administrator_name" label="操作者" width="120" />
            <el-table-column prop="create_time" label="创建时间" width="190" />
            <el-table-column prop="update_time" label="修改时间" width="190" />
            <el-table-column prop="remark" label="备注" width="200" show-overflow-tooltip />
            <el-table-column label="操作" width="120" fixed="right">
                <template #default="scope">
                    <span>
                        <el-text type="primary" style="cursor: pointer;" @click="openDialogOfUpdMaterial(scope.row)">修改</el-text>
                    </span>
                    <span style="margin-left: 10px;">
                        <el-text type="primary" style="cursor: pointer;" @click="openDialogOfDelMaterial(scope.row)">删除</el-text>
                    </span>

                </template>
            </el-table-column>

        </el-table>
        <div style="margin-top: 5px;">
            <el-pagination size="small" background layout="prev, pager, next,->,total" :total="page.total"
                @current-change="pageChange" v-model:page-size="page.pageSize"
                v-model:current-page="page.currentPage" />
        </div>
        <!-- 驳回的对话框 -->
        <el-dialog title="添加类别" v-model="dialogOfCategory" width="30%">
            <div class="dialog-body">
                <el-input v-model="subCategory.name" placeholder="材料类别"></el-input>
                <el-input v-model="subCategory.remark" placeholder="备注" style="margin-top: 10px;"></el-input>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialog.visible = false">取消</el-button>
                    <el-button type="primary" @click="addCategory">
                        添加
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
    <!-- 新增物料的弹出框 -->
    <div>
        <el-dialog v-model="dialogOfMaterial" title="新增物料" width="800" draggable overflow>
            <span>
                <el-form :model="NewMaterialMsg" label-width="auto" style="max-width: 600px;margin: auto;">
                    <el-form-item label="物料名称" style="width: 300px;">
                        <el-input v-model="NewMaterialMsg.material_name" />
                    </el-form-item>
                    <el-form-item label="物料类别">
                        <el-select v-model="NewMaterialMsg.category_id" placeholder="请选择物料类别" style="width: 230px;"
                            clearable>
                            <el-option v-for="item in categoryShow" :key="item.id" :label="item.name"
                                :value="item.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="最低库存">
                        <el-input v-model="NewMaterialMsg.min_stock"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="保质期">
                        <el-input v-model="NewMaterialMsg.shelf_life"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;天</span>
                    </el-form-item>
                    <el-form-item label="箱子体积">
                        <el-input v-model="NewMaterialMsg.box_volume"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;平方米</span>
                    </el-form-item>
                    <el-form-item label="箱子件数">
                        <el-input v-model="NewMaterialMsg.box_quantity"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="单位名称">
                        <el-input v-model="NewMaterialMsg.unit" style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="NewMaterialMsg.remark"
                            style="width: 300px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>

                </el-form>
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="closeAddNewMaterial">关闭</el-button>
                    <el-button type="primary" @click="addNewMaterial">
                        新增
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
     <!-- 修改物料的弹出框 -->
     <div>
        <el-dialog v-model="dialogOfMaterialUpd" title="新增物料" width="800" draggable overflow>
            <span>
                <el-form :model="materialUpdMsg" label-width="auto" style="max-width: 600px;margin: auto;">
                    <el-form-item label="物料名称" style="width: 300px;">
                        <el-input v-model="materialUpdMsg.material_name" />
                    </el-form-item>
                    <el-form-item label="物料类别">
                        <el-select v-model="materialUpdMsg.category_id" placeholder="请选择物料类别" style="width: 230px;"
                            clearable>
                            <el-option v-for="item in categoryShow" :key="item.id" :label="item.name"
                                :value="item.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="最低库存">
                        <el-input v-model="materialUpdMsg.min_stock"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="保质期">
                        <el-input v-model="materialUpdMsg.shelf_life"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;天</span>
                    </el-form-item>
                    <el-form-item label="箱子体积">
                        <el-input v-model="materialUpdMsg.box_volume"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;平方米</span>
                    </el-form-item>
                    <el-form-item label="箱子件数">
                        <el-input v-model="materialUpdMsg.box_quantity"
                            style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="单位名称">
                        <el-input v-model="materialUpdMsg.unit" style="width: 150px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="materialUpdMsg.remark"
                            style="width: 300px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>

                </el-form>
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="closeUpdNewMaterial">关闭</el-button>
                    <el-button type="primary" @click="updateNewMaterial">
                        修改
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
     <!-- 删除物料的弹出框 -->
     <div>
        <el-dialog v-model="dialogOfMaterialDel" title="删除物料" width="500" draggable overflow>
            <span >
               <div style="text-align:center;padding-bottom:10px;margin-top: 10px;">
                确认要删除<span style="color: blue;">{{ materialDelMsg.material_name }}</span>({{ materialDelMsg.remark }})吗？
            </div>
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="closeDelMaterial">取消</el-button>
                    <el-button type="primary" @click="deleteMaterial">
                        确认
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
const useUserStore = userStore()
const page = ref({ currentPage: 1, pageSize: 7, total: 0 })
const searchMsg = ref({ material_name: '', category_id: '' }) //搜索
const dialogOfCategory = ref(false)   //弹出框新增分类
const dialogOfMaterialDel = ref(false)  //弹出框删除材料
const dialogOfMaterialUpd = ref(false)
const dialogOfMaterial = ref(false)   //弹出框新增材料
const subCategory = ref({ name: '', remark: '' })
const categoryShow = ref([])
const materialShow = ref([])
const NewMaterialMsg = ref({})
const materialDelMsg = ref({})
const materialUpdMsg = ref({})

//初始化
onMounted(() => {
    show()
}
)


//添加新分类
const openDialogOfAddCategory = () => {
    dialogOfCategory.value = true
    subCategory.value = { name: '', remark: '' }
}
const addCategory = () => {
    if (subCategory.value.name.length == 0) {
        ElMessage.warning("材料类别不可为空")
        return
    }

    http.post("/category/add", subCategory.value).then(res => {
        if (res.code == 200) {
            ElMessage.success("添加成功")
            dialogOfCategory.value = false
            show()
        }

    })
}
//添加新物料
const openDialogOfAddMaterial = () => {
    NewMaterialMsg.value = {}
    dialogOfMaterial.value = true;
}
const closeAddNewMaterial = () => {
    dialogOfMaterial.value = false;
}
const addNewMaterial = () => {
    NewMaterialMsg.value.administrator_id = useUserStore.user.id
    http.post('/material/add', NewMaterialMsg.value).then(res => {
        if (res.code == 200) {
            dialogOfMaterial.value = false;
            show()
        }
    })
}
//删除物料
const openDialogOfDelMaterial = (data) => {
    dialogOfMaterialDel.value = true
    materialDelMsg.value = data
    console.log(data)
}
const closeDelMaterial = () => {
    dialogOfMaterialDel.value = false
}
const deleteMaterial = () => {
    http.post('/material/delete',materialDelMsg.value).then(res => {
        if (res.code == 200) {
            dialogOfMaterialDel.value = false
            show()
        }
    })
}
//修改物料
const openDialogOfUpdMaterial = (data) => {
    materialUpdMsg.value = data
    dialogOfMaterialUpd.value = true
}
const closeUpdNewMaterial = () => {
    dialogOfMaterialUpd.value = false
}
const updateNewMaterial = () => {
    materialUpdMsg.value.administrator_id = useUserStore.user.id
    http.post('/material/update', materialUpdMsg.value).then(res => {
        if (res.code == 200) {
            dialogOfMaterialUpd.value = false
            show()
        }
    })
}
//初始化获得页面信息
const show = () => {
    http.post('/material/show', {
        material_name: searchMsg.value.material_name,
        category_id: searchMsg.value.category_id,
        currentPage: page.value.currentPage,
        pageSize: page.value.pageSize,
        
        
    }).then(res => {
        if (res.code == 200) {
            categoryShow.value = res.data.categoryList
            materialShow.value = res.data.materialList
            page.value.total = res.data.count
            console.log(materialShow.value)
        }
    })
}
const search = () => {
    page.value.currentPage = 1
    show()
}
const pageChange = () => {
    show()
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
    font-size: 16px;
    font-weight: bold;
    padding-left: 20%;
    padding-right: 20%;
}

.btn-label {
    cursor: pointer;
}

.btn-label::selection {
    background: transparent;
    color: inherit;
}

:deep(.el-table .el-table__row) {

    height: 50px;
}
</style>