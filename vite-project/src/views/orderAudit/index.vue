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
                <el-option label="出售" value="SE" />
                <el-option label="销毁" value="DE" />
            </el-select>
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
            <el-table-column prop="io_time" label="出入库时间" width="180" />
            <el-table-column prop="operator_name" label="操作者" width="180" />
            <el-table-column label="操作">
                <template #default="scope">
                    <span>
                        <el-text type="primary" style="cursor: pointer;"
                            @click="openAuditDialog(scope.row.stock_io_id)">审核</el-text>
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
    <!-- 审核的弹出框 -->
    <div>
        <el-dialog v-model="dialogAudit" title="审核" width="1000" style="max-height: 800px;" draggable overflow>
            <span>
                <div class="aduit_show_box">
                    <div class="aduit_show">
                        <div>
                            <span class="aduit_lable">订单id&nbsp;:</span>
                            <span>{{ AduitData.stock_io_id }}</span>
                        </div>

                        <div>
                            <span class="aduit_lable">当前仓库&nbsp;:</span>
                            <span>{{ AduitData.to_warehouse_name }}</span>
                        </div>


                        <div>
                            <span class="aduit_lable">出入库类型&nbsp;:</span>
                            <span v-if="AduitData.sub_type == 'DP'">直购</span>
                            <span v-if="AduitData.sub_type == 'TR'">调拨</span>
                            <span v-if="AduitData.sub_type == 'SE'">出售</span>
                            <span v-if="AduitData.sub_type == 'DE'">销毁</span>
                        </div>

                        <div>
                            <span class="aduit_lable">备注&nbsp;:</span>
                            <span v-if="AduitData.remark != null">{{ AduitData.remark }}</span>
                            <span v-if="AduitData.remark == null">无</span>
                        </div>
                    </div>
                    <div class="aduit_show">
                        <div>
                            <span class="aduit_lable">操作者&nbsp;:</span>
                            <span>{{ AduitData.operator_name }}</span>
                        </div>
                        <div v-if="AduitData.sub_type == 'TR'">
                            <span class="aduit_lable">调拨仓库&nbsp;:</span>
                            <span>{{ AduitData.from_warehouse_name }}</span>
                        </div>
                        <div>
                            <span class="aduit_lable">出入库时间&nbsp;:</span>
                            <span>{{ AduitData.io_time }}</span>
                        </div>
                    </div>
                </div>
                <div style="margin: auto;padding-top: 5px;">
                    <el-table :data="AduitData.stockIODetail" style="width:80%;margin: auto;max-height: 250px;" border>
                        <el-table-column prop="material_name" label="名称" width="80" />
                        <el-table-column prop="category_name" label="类型" width="100" />
                        <el-table-column prop="volume_per_unit" label="规格" width="180" />
                        <el-table-column prop="production_date" label="生产日期" />
                        <el-table-column prop="shelf_life" label="保质期" width="80" />
                        <el-table-column prop="quantity" label="数量" width="80" />
                        <el-table-column prop="unit_price" :label="AduitData.sub_type === 'DE' ? '出售价' : '购入价'"
                            width="80" />
                        <template #append>
                            <tr class="total-row" style="text-align: center;line-height: 40px;height: 40px;">
                                <td style="font-weight: bold;width: 510px;">
                                    总计/元
                                </td>
                                <td style="width: 100px;">
                                    ￥{{ AduitData.total_price }}
                                </td>
                            </tr>
                        </template>
                    </el-table>
                </div>
            </span>
            <div class="aduit_box">
                <div>
                    <el-radio-group v-model="AduitSubmitData.audit_status">
                        <el-radio-button label="通过" value="APPROVED" />
                        <el-radio-button label="拒绝" value="REJECTED" />
                    </el-radio-group>
                </div>
                <div style="padding-top: 5px;">
                    <el-input v-model="AduitSubmitData.audit_comment" type="textarea" placeholder="审核理由" />
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogAudit = false">取消</el-button>
                    <el-button type="primary" @click="submitAduit">
                        确定
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import http from '../../untils/http.js';
import { userStore } from '../../store/user'
import { ElMessage } from 'element-plus'

const useUserStore = userStore()
const argumentData = ref({
    warehouseList: [],
})
const dialogAudit = ref(false)
const MainData = ref({
    page: { total: 0, currentPage: 1, pageSize: 7 }
    , StockIODataShow: []
    , search_stock_io_id: ''
    , search_warehouse_id: ''
    , search_status: ''
    , opr_stock_io_id: ''
})
const AduitData = ref({})
const AduitSubmitData = ref({
    audit_status: 'APPROVED'
})
onMounted(() => {
    pageShow()
})
// 打开审核的弹出框
const openAuditDialog = (val) => {
    http.post('/stockIO/getAuditMsg', {
        stock_io_id: val,
    }).then(res => {
        if (res.code == 200) {
            // 审核数据
            AduitData.value = res.data.stockIO
            AduitData.value.stockIODetail = res.data.stockIODetail
            AduitData.value.total_price = AduitData.value.stockIODetail.reduce((pre, cur) => {
                return pre + cur.unit_price * cur.quantity
            }, 0)
            // 审核提交数据
            AduitSubmitData.value = {
                audit_status: 'APPROVED',
                stock_io_id: AduitData.value.stock_io_id,
                auditor_id : useUserStore.user.id
            }
        }
    })
    dialogAudit.value = true
}
// 审核提交
const submitAduit = () => {
    http.post('/stockIO/submitForAudit', AduitSubmitData.value).then(res => {
        if (res.code == 200) {
            ElMessage.success('提交成功')
            dialogAudit.value = false
            pageShow()
        }
    })
}
// 基础页面显示
const pageShow = async (page) => {
    if (page) {
        MainData.value.page.currentPage = page
    }
    http.post('/stockIO/auditShow', {
        stock_io_id: MainData.value.search_stock_io_id,
        to_warehouse_id: MainData.value.search_warehouse_id,
        sub_type: MainData.value.search_sub_type,
        status: MainData.value.search_status,
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
.aduit_show_box {
    padding-left: 10%;
    width: 80%;
    display: flex;
}

.aduit_show {
    font-size: 15px;
    width: 350px;
}

.aduit_lable {
    display: inline-block;
    letter-spacing: 2px;
    text-align: right;
    font-weight: bold;
    padding: 1px;
    width: 100px;
}

.aduit_box {
    padding-left: 10%;
    width: 80%;
    padding-top: 10px;
}
</style>