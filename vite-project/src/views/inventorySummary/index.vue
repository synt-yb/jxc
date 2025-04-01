<template>
    <div style="padding-top: 20px;padding-left: 15%;padding-right: 15%;padding-bottom: 50px;">
        <div style="padding-top: 30px;padding-bottom: 5px;">
            <el-select v-model="MainData.search_warehouse_id" placeholder="仓库名称" style="width: 120px"
                @change="pageShow()">
                <el-option v-for="item in argumentData.warehouseList" :label="item.warehouse_name"
                    :value="item.warehouse_id" />
            </el-select>
            <el-select v-model="MainData.search_category_id" style="width: 120px;padding-left: 20px;"
                @change="pageShow()">
                <el-option label="全部类型" value="" />
                <el-option v-for="item in argumentData.categoryList" :label="item.name" :value="item.id" />
            </el-select>
            <el-button type="primary" style="background-color: red;" @click="inventoryPDF">生成库存pdf</el-button>
        </div>
        <el-table :data="MainData.inventoryList" border style="max-height: 500px;" :span-method="arraySpanMethod">
            <el-table-column prop="material_name" label="物料名称" width="120" />
            <el-table-column prop="category_name" label="类型" width="100" />
            <el-table-column prop="volume_per_unit" label="规格" width="200" />
            <el-table-column prop="purchase_price" label="购入价/元" width="100" />
            <el-table-column prop="quantity" label="数量/件" width="80" />
            <el-table-column prop="production_date" label="生产日期" />
            <el-table-column prop="shelf_life" label="保质期/天" width="130" />
        </el-table>

    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { userStore } from '../../store/user'
import http from '../../untils/http'
import { ElButton } from 'element-plus'
onMounted(() => {
    pageShow()
})
const useUserStore = userStore()
const argumentData = ref({
    warehouseList: [],
    categoryList: [],
})
const MainData = ref({
    inventoryList: [],
    search_warehouse_id: '',
    search_category_id: ''
})
// 页面显示
const pageShow = async () => {
    http.post('/inventory/show', {
        user_id: useUserStore.user.id,
        warehouse_id: MainData.value.search_warehouse_id,
        category_id: MainData.value.search_category_id
    }).then(res => {
        if (res.code == 200) {
            argumentData.value.warehouseList = res.data.warehouseList
            argumentData.value.categoryList = res.data.categoryList
            MainData.value.search_warehouse_id = res.data.warehouse_id
            MainData.value.inventoryList = res.data.inventoryList
        }
    })
}
// 生成库存pdf
const inventoryPDF = async () => {
    console.log(MainData.value.search_warehouse_id);
  try {
    const res = await http.post('/inventory/pdf', {
      warehouse_id: MainData.value.search_warehouse_id
    }); // 使用JSON格式发送数据
    if (res.code === 200) {
      const byteCharacters = atob(res.data.pdfContent);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const blob = new Blob([byteArray], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'warehouse_inventory.pdf';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } else {
      console.error('生成PDF失败:', res.msg);
    }
  } catch (error) {
    console.error('生成PDF时出错:', error);
  }
};
// 合并单元格
const arraySpanMethod = ({ row, column, rowIndex, columnIndex }) => {
    if (columnIndex < 3) {
        let rowspan = 1;
        // 计算相同 material_id 的行数
        for (let i = rowIndex + 1; i < MainData.value.inventoryList.length; i++) {
            if (MainData.value.inventoryList[i].material_id === row.material_id) {
                rowspan++;
            } else {
                break;
            }
        }
        if (rowIndex > 0 && MainData.value.inventoryList[rowIndex].material_id === MainData.value.inventoryList[rowIndex - 1].material_id) {
            // 如果当前行不是相同 material_id 的第一行，则隐藏该单元格
            return {
                rowspan: 0,
                colspan: 0
            };
        } else {
            // 如果是相同 material_id 的第一行，则设置合并的行数
            return {
                rowspan: rowspan,
                colspan: 1
            };
        }
    }
    // 非前三列不进行合并
    return {
        rowspan: 1,
        colspan: 1
    };
};
</script>
<style scoped></style>