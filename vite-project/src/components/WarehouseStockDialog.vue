<template>
  <el-dialog 
      :title="dialogTitle" 
      :visible="dialogVisible" 
      @update:visible="updateVisible"
      :width="dialogWidth" 
      draggable
  >
      <div style="margin-left: 5%;margin-right: 5%;">
          <div style="padding-bottom: 10px;">
              <el-select 
                  v-model="searchCategoryId" 
                  placeholder="类型" 
                  style="width: 100px"
                  @change="getDPMSg"
              >
                  <el-option 
                      v-for="item in categoryShow" 
                      :key="item.id" 
                      :label="item.name"
                      :value="item.id" 
                  />
              </el-select>
              <el-input 
                  v-model="searchMaterialName" 
                  placeholder="请输入物料名称"
                  style="width: 200px;padding-left: 10px;" 
                  clearable 
              />
              <el-button 
                  type="primary" 
                  style="margin-left: 10px;" 
                  @click="getDPMSg"
              >
                  搜索
              </el-button>
          </div>
          <el-table 
              :data="materialShow" 
              height="200" 
              @current-change="DPOfChoseMaterial"
              style="width: 100%;margin-bottom: 10px;" 
              border
          >
              <el-table-column label="名称" prop="material_name" />
              <el-table-column label="保质期/天" prop="shelf_life" />
              <el-table-column label="规格" prop="volume_per_unit" />
          </el-table>
          <div>
              <span>
                  物料名称：
                  <el-input v-model="submitMsg.material_name" placeholder="物料名称" style="width: 200px;margin-right: 20px;"  readonly></el-input>
              </span>
              <span>
                  生产日期：
                  <el-date-picker v-model="submitMsg.production_date" type="datetime" value-format="YYYY/MM/DD hh:mm:ss " />
              </span>
          </div>
          <div style="padding-top: 10px;">
              <span style="padding-left: 30px;">
                  单价：
                  <el-input 
                      v-model="submitMsg.unit_price" 
                      placeholder="单价/元"
                      style="width: 100px;margin-right: 20px;"
                  ></el-input>
              </span>
              <span>
                  件数：
                  <el-input 
                      v-model="submitMsg.quantity" 
                      placeholder="件数" 
                      style="width: 100px;"
                  ></el-input>
              </span>
          </div>
      </div>
      <template #footer>
          <div class="dialog-footer">
              <el-button @click="closeDialog">取消</el-button>
              <el-button type="primary" @click="addDetails">
                  添加
              </el-button>
          </div>
      </template>
  </el-dialog>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';

// 接收父组件传来的参数
const props = defineProps({
  dialogTitle: {
      type: String,
      default: '添加物料'
  },
  dialogWidth: {
      type: String,
      default: '800px'
  },
  dialogVisible: {
      type: Boolean,
      default: false
  },
  categoryShow: {
      type: Array,
      default: () => []
  },
  materialShow: {
      type: Array,
      default: () => []
  }
});

const emits = defineEmits(['update:dialogVisible', 'add', 'search']);

const searchCategoryId = ref(props.categoryShow.length > 0 ? props.categoryShow[0].id : null);
const searchMaterialName = ref('');
const submitMsg = ref({
  material_name: '',
  production_date: '',
  unit_price: '',
  quantity: ''
});

const getDPMSg = () => {
  emits('search', {
      categoryId: searchCategoryId.value,
      materialName: searchMaterialName.value
  });
};

const DPOfChoseMaterial = (row) => {
  submitMsg.value.material_name = row.material_name;
};

const closeDialog = () => {
  emits('update:dialogVisible', false);
};

const addDetails = () => {
  emits('add', submitMsg.value);
  emits('update:dialogVisible', false);
};

const updateVisible = (newValue) => {
  emits('update:dialogVisible', newValue);
};
</script>    