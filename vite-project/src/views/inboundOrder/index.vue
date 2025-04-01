<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;padding-bottom: 50px;">
        <el-card style="max-width: 100%;">
            <template #header>
                <div class="card-header">
                    <span>查询条件</span>
                </div>
            </template>
            <div style="text-align: center;">
                <span>单号</span>
                <el-input v-model="MainData.search_stock_io_id" style="width: 400px;margin-left: 30px;" clearable />
                <el-button type="primary" style="margin-left: 30px;" @click="pageShow(1)">查询</el-button>
            </div>

        </el-card>
        <div style="padding-top: 30px;padding-bottom: 5px;">
            <el-select v-model="MainData.search_warehouse_id" placeholder="仓库名称" style="width: 120px"
                @change="pageShow(1)">
                <el-option v-for="item in argumentData.warehouseList" :label="item.warehouse_name"
                    :value="item.warehouse_id" />
            </el-select>
            <el-select v-model="MainData.search_sub_type" style="width: 120px;padding-left: 20px;"
                @change="pageShow(1)">
                <el-option label="全部类型" value="" />
                <el-option label="直购" value="DP" />
                <el-option label="调拨" value="TR" />
            </el-select>
            <el-select v-model="MainData.search_status" placeholder="状态" style="width: 120px;padding-left: 20px;"
                @change="pageShow(1)">
                <el-option label="全部" value="" />
                <el-option label="草稿" value="DRAFT" />
                <el-option label="待审核" value="PENDING" />
                <el-option label="已通过" value="APPROVED" />
                <el-option label="已拒绝" value="REJECTED" />
            </el-select>
            <el-button type="primary" style="margin-left: 30px;background-color: #ffa500;"
                @click="openAddStockIODialog()">创建草稿</el-button>
        </div>
        <el-table :data="MainData.StockIODataShow" border style="width: 100%" stripe>

            <el-table-column prop="stock_io_id" label="单号" width="200" show-overflow-tooltip fixed />
            <el-table-column label="类型" width="70">
                <template #default="scope">
                    <span v-if="scope.row.sub_type == 'DP'">直购</span>
                    <span v-if="scope.row.sub_type == 'TB'"> 调拨</span>
                </template>
            </el-table-column>
            <el-table-column prop="total_price" label="总花费" width="80" />
            <el-table-column prop="create_time" label="创建时间" width="180" />
            <el-table-column prop="update_time" label="修改时间" width="180" />
            <el-table-column label="状态" width="70">
                <template #default="scope">
                    <span v-if="scope.row.status == 'DRAFT'" style="color: #ffa500;">草稿</span>
                    <span v-if="scope.row.status == 'PENDING'" style="color:darkgrey;"> 待审核</span>
                    <span v-if="scope.row.status == 'APPROVED'" style="color: greenyellow;">已通过</span>
                    <span v-if="scope.row.status == 'REJECTED'" style="color:crimson;"> 已拒绝</span>
                </template>
            </el-table-column>
            <el-table-column label="操作">
                <template #default="scope">
                    <span v-if="scope.row.status != 'APPROVED'">
                        <el-text type="primary" style="cursor: pointer;"
                            @click="openUpdStockIODialog(scope.row.stock_io_id)">修改</el-text>
                    </span>
                    <span style="margin-left: 10px;" v-if="scope.row.status == 'DRAFT'">
                        <el-text type="primary" style="cursor: pointer;"
                            @click="openDelOrSubDialog(scope.row.stock_io_id, 'sub')">提交</el-text>
                    </span>
                    <span style="margin-left: 10px;" v-if="scope.row.status != 'APPROVED'">
                        <el-text type="primary" style="cursor: pointer;"
                            @click="openDelOrSubDialog(scope.row.stock_io_id, 'del')">删除</el-text>
                    </span>

                </template>
            </el-table-column>

        </el-table>
        <div style="margin-top: 5px;">
            <el-pagination size="small" background layout="prev, pager, next,->,total" :total="MainData.page.total"
                @current-change="pageShow()" v-model:page-size="MainData.page.pageSize"
                v-model:current-page="MainData.page.currentPage" />
        </div>
    </div>
    <!-- 创建草稿和修改订单的弹出框 -->
    <div>
        <el-dialog v-model="dialogData.dialogOfAddOrUpd.visible" :title="dialogData.dialogOfAddOrUpd.title" width="1000"
        style="max-height: 1000px;"
            draggable overflow>
            <span>
                <el-form label-width="auto" style="max-width: 600px;margin: auto;">
                    <el-form-item label="订单号" v-if="INCreateData.stock_io_id!=null">
                        <el-input v-model="INCreateData.stock_io_id" style="width: 300px;" disabled/>
                    </el-form-item>
                    <el-form-item label="类型">
                        <el-radio-group v-model="INCreateData.sub_type" @change="clearINCreateData" :disabled="INCreateData.stock_io_id">
                            <el-radio-button label="直购" value="DP" />
                            <el-radio-button label="调拨" value="TR" />
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="调拨来源" v-if="INCreateData.sub_type == 'TR'">
                        <el-select v-model="INCreateData.category_id" placeholder="请选择仓库" style="width: 230px;"
                            clearable>
                            <el-option v-for="item in argumentData.categoryShow" :key="item.id" :label="item.name"
                                :value="item.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="出入库时间">
                        <el-date-picker v-model="INCreateData.io_time" type="datetime"
                            value-format="YYYY-MM-DD hh:mm:ss" />
                    </el-form-item>
                    <el-form-item label="备注">
                        <el-input v-model="INCreateData.remark" style="width: 300px;" /><span>&nbsp;&nbsp;&nbsp;</span>
                    </el-form-item>
                </el-form>
                <div style="margin: auto;text-align: center;">
                    <el-table :data="INCreateData.stockIODetail" style="width: 80%;margin: auto;" max-height="250"
                        border>
                        <el-table-column prop="material_name" label="物料名称" width="120" />
                        <el-table-column prop="volume_per_unit" label="规格" width="180" />
                        <el-table-column prop="production_date" label="生产日期" width="150" />
                        <el-table-column prop="shelf_life" label="保质期" width="80" />
                        <el-table-column prop="quantity" label="数量" width="80" />
                        <el-table-column prop="unit_price" label="价格" width="80" />
                        <el-table-column fixed="right" label="操作" min-width="50">
                            <template #default="scope">
                                <el-button link type="primary" @click.prevent="delDetails(scope.row)">
                                    删除
                                </el-button>
                            </template>
                        </el-table-column>
                        <template #append>
                            <tr class="total-row" style="text-align: center;line-height: 40px;height: 40px;">
                                <td style="font-weight: bold;width: 510px;">
                                    总计
                                </td>
                                <td style="width: 100px;">
                                    {{ INCreateData.total_price }}
                                </td>
                            </tr>
                        </template>
                    </el-table>
                    <el-button class="mt-4" style="width: 80%;margin-left: 0px;margin-top: 10px;
                border: gray solid 1px;font-weight:bold;" @click="openMaterialDPDialog">
                        添加
                    </el-button>
                </div>
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogData.dialogOfAddOrUpd.visible = false">取消</el-button>
                    <el-button type="primary" @click="create()" v-if="INCreateData.stock_io_id==null">
                        添加草稿
                    </el-button>
                    <el-button type="primary" @click="update()" v-if="INCreateData.stock_io_id!=null">
                        修改
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
    <!-- 直购的添加物料的对话框 -->
    <div>
        <el-dialog title="添加物料" v-model="dialogData.dialogOfCategoryDp" width="800px" draggable>
            <div style="margin-left: 5%;margin-right: 5%;">
                <div style="padding-bottom: 10px;">
                    <el-select v-model="DPData.search_category_id" placeholder="类型" style="width: 100px"
                        @change="getDPMSg()">
                        <el-option v-for="item in DPData.category_show" :key="item.id" :label="item.name"
                            :value="item.id" />
                    </el-select>
                    <el-input v-model="DPData.search_material_name" placeholder="请输入物料名称"
                        style="width: 200px;padding-left: 10px;" clearable />
                    <el-button type="primary" style="margin-left: 10px;" @click="getDPMSg()">搜索</el-button>
                </div>
                <el-table :data="DPData.material_show" height="200" @current-change="DPOfChoseMaterial"
                    style="width: 100%;margin-bottom: 10px;" border>
                    <el-table-column label="名称" prop="material_name" />
                    <el-table-column label="保质期/天" prop="shelf_life" />
                    <el-table-column label="规格" prop="volume_per_unit" />
                </el-table>
                <div>
                    <span>
                        物料名称：
                        <el-input v-model="DPData.submitMsg.material_name" placeholder="物料名称"
                            style="width: 200px;margin-right: 20px;" readonly></el-input>
                    </span>
                    <span>
                        生产日期：
                        <el-date-picker v-model="DPData.submitMsg.production_date" type="datetime"
                            value-format="YYYY/MM/DD hh:mm:ss " />
                    </span>
                </div>
                <div style="padding-top: 10px;">
                    <span style="padding-left: 30px;">
                        单价：
                        <el-input v-model="DPData.submitMsg.unit_price" placeholder="单价/元"
                            style="width: 100px;margin-right: 20px;"></el-input>
                    </span>
                    <span>
                        件数：
                        <el-input v-model="DPData.submitMsg.quantity" placeholder="件数" style="width: 100px;"></el-input>
                    </span>
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogData.dialogOfCategoryDp = false">取消</el-button>
                    <el-button type="primary" @click="addDetails()">
                        添加
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>

    <!-- 确认删除的弹出框 -->
    <div>
        <el-dialog v-model="dialogData.dialogOfDelOrSub.visible" title="确认" width="500" draggable overflow>
            <span>
                <div style="text-align:center;padding-bottom:10px;margin-top: 10px;">
                    确认{{ dialogData.dialogOfDelOrSub.keyword }}
                    <span style="color: blue;">订单{{ MainData.opr_stock_io_id }}</span>?
                </div>
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogData.dialogOfDelOrSub.visible = false">取消</el-button>
                    <el-button type="primary" @click="delIN()" v-if="dialogData.dialogOfDelOrSub.keyword === '删除'">
                        删除
                    </el-button>
                    <el-button type="primary" @click="subIN()" v-if="dialogData.dialogOfDelOrSub.keyword === '提交'">
                        提交
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus';
import { userStore } from '../../store/user'
import http from '../../untils/http.js';

//变量池
const useUserStore = userStore()
const argumentData = ref({
    warehouseList: [],
    categoryShow:[]
})
const dialogData = ref({   //<弹出框管理>
    dialogOfStockIN: false,
    dialogOfDelOrSub: { visible: false, keyword: '删除' },
    dialogOfAddOrUpd: { visible: false, title: '' },
    dialogOfCategoryDp: false
})
const MainData = ref({
    search_stock_io_id: '',//搜索入库单id
    search_warehouse_id: '',//搜索仓库id
    search_sub_type: '', //搜索类型(直购或其他)
    search_status: '', //搜索状态(草稿或其他)
    StockIODataShow: [],
    opr_stock_io_id: '',//删除或确认的订单id
    page: { currentPage: 1, pageSize: 7, total: 0 }
})
const DPData = ref({  //直购的页面信息
    search_category_id: '',
    search_material_name: '',
    category_show: [],
    material_show: [],
    submitMsg: {}
})
const INCreateData = ref({  //添加物料的页面信息
    sub_type: 'DP',
    stockIODetail: [],
    total_price: 0
})

//页面初始化
onMounted(() => {
    //当前用户的管理仓库，显示的数据，分页信息
    pageShow()
})


//  打开对话框:提交和删除---创建草稿----修改订单----直购的添加物料
const openDelOrSubDialog = (val, ch) => {
    dialogData.value.dialogOfDelOrSub.keyword = ch == 'del' ? '删除' : '提交'
    MainData.value.opr_stock_io_id = val
    dialogData.value.dialogOfDelOrSub.visible = true
}
const openAddStockIODialog = () => {
    clearINCreateData('DP')
    dialogData.value.dialogOfAddOrUpd.title = "创建草稿"
    dialogData.value.dialogOfAddOrUpd.visible = true
}
const openUpdStockIODialog = (val) => {
    clearINCreateData()
    dialogData.value.dialogOfAddOrUpd.title = "修改订单申请"
    http.post('/stockIO/getUpdMsg', { stock_io_id: val }).then(res => {
        if (res.code == 200) {
            INCreateData.value = res.data.stockIO
            INCreateData.value.stockIODetail = res.data.stockIODetail
            INCreateData.value.total_price = INCreateData.value.stockIODetail.reduce((pre, cur) => {
                return pre + cur.unit_price * cur.quantity
            }, 0)
            dialogData.value.dialogOfAddOrUpd.visible = true
        }
    })
}
const openMaterialDPDialog = () => {
    DPData.value.submitMsg = {}
    dialogData.value.dialogOfCategoryDp = true
    getDPMSg()
}
// 删除订单 -- 提交草稿 -- 创建订单草稿及相关操作
const delIN = () => {
    http.post('/stockIO/delete', { stock_io_id: MainData.value.opr_stock_io_id }).then(res => {
        if (res.code == 200) {
            ElMessage.success('删除成功')
            dialogData.value.dialogOfDelOrSub.visible = false
            pageShow()
        }
    })
}
const subIN = () => {
    http.post('/stockIO/submit', { stock_io_id: MainData.value.opr_stock_io_id }).then(res => {
        if (res.code == 200) {
            ElMessage.success('提交成功')
            dialogData.value.dialogOfDelOrSub.visible = false
            pageShow()
        }
    })
}
const create = () => { //新增草稿
    INCreateData.value.operator_id = useUserStore.user.id
    INCreateData.value.to_warehouse_id = MainData.value.search_warehouse_id
    http.post('/stockIO/addDpIO', INCreateData.value, true).then(res => {
        if (res.code == 200) {
            ElMessage.success('创建成功')
            dialogData.value.dialogOfAddOrUpd.visible = false
            pageShow()
        }
    })
}
const update = ()=>{
    console.log(INCreateData.value)
    http.post('/stockIO/update',INCreateData.value,true).then(res=>{
        if(res.code==200){
            ElMessage.success('修改成功')
            dialogData.value.dialogOfAddOrUpd.visible = false
            pageShow()
        }
    })
}
const clearINCreateData = (val) => {//新增草稿相关操作
    INCreateData.value = {
        sub_type: val,
        stockIODetail: []
    }
}

// 直购： 往订单详情添加物料，删除物料，获得初期的页面信息
const addDetails = () => {
    //前端审核
    if (DPData.value.submitMsg.material_name == null || DPData.value.submitMsg.unit_price == null ||
        DPData.value.submitMsg.quantity == null || DPData.value.submitMsg.production_date == null) {
        ElMessage.error('请输入完整信息')
        return
    }
    INCreateData.value.stockIODetail.push(DPData.value.submitMsg)
    INCreateData.value.total_price = INCreateData.value.stockIODetail.reduce((pre, cur) => {
        return pre + cur.unit_price * cur.quantity
    }, 0)
    dialogData.value.dialogOfCategoryDp = false
}
const delDetails = (val) => {
    INCreateData.value.stockIODetail.splice(val, 1)
    INCreateData.value.total_price = INCreateData.value.stockIODetail.reduce((pre, cur) => {
        return pre + cur.unit_price * cur.quantity
    }, 0)
}
const getDPMSg = () => {  //获取直购的初期页面信息
    DPData.value.submitMsg = {}
    http.post('/material/getDPMsg', {
        material_name: DPData.value.search_material_name,
        category_id: DPData.value.search_category_id,
    }).then(res => {
        if (res.code == 200) {
            DPData.value.category_show = res.data.categoryList
            DPData.value.material_show = res.data.materialList
            DPData.value.search_category_id = res.data.first_category_id
        }
    })
}
const DPOfChoseMaterial = (val) => {  //选择添加物料的添加键
    if (val == null) {
        return
    }
    if (DPData.value.submitMsg.material_id !== val.material_id) {
        DPData.value.submitMsg = {}
        DPData.value.submitMsg.material_id = val.material_id
        DPData.value.submitMsg.material_name = val.material_name
        DPData.value.submitMsg.shelf_life = val.shelf_life
        DPData.value.submitMsg.volume_per_unit = val.volume_per_unit
    }

}
//初始化获得页面信息
const pageShow = (val) => {
    if (val != null) {
        MainData.value.page.currentPage = 1
    }

    http.post('/stockIO/show', {
        //当前用户的id
        operator_id: useUserStore.user.id,
        //入库单类型
        type: 'IN',
        // 按条件查询的信息
        stock_io_id: MainData.value.search_stock_io_id,
        warehouse_id: MainData.value.search_warehouse_id,
        sub_type: MainData.value.search_sub_type,
        status: MainData.value.search_status,
        // 分页信息
        currentPage: MainData.value.page.currentPage,
        pageSize: MainData.value.page.pageSize,
    }).then(res => {
        if (res.code == 200) {
            argumentData.value.warehouseList = res.data.warehouseList
            MainData.value.search_warehouse_id = res.data.warehouse_id
            MainData.value.StockIODataShow = res.data.stockIOList
            MainData.value.page.total = res.data.count
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