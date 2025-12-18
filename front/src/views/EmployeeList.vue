<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>员工管理</span>
        <div>
          <el-input
            v-model="searchName"
            placeholder="按员工姓名搜索"
            style="width: 200px; margin-right: 10px;"
            @change="handleSearch"
            clearable
          />
          <el-button type="primary" @click="dialogVisible = true">新增员工</el-button>
        </div>
      </div>
    </template>

    <el-table :data="employeeList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="180" />
      <el-table-column prop="category_name" label="擅长领域" width="180" />
      <el-table-column prop="phone" label="联系电话" />

      <el-table-column label="操作" width="120" fixed="right">
        <template #default="scope">
          <el-popconfirm
            title="确定要注销(删除)该员工吗？"
            @confirm="deleteEmployee(scope.row.id)"
            confirm-button-text="确定"
            cancel-button-text="取消"
          >
            <template #reference>
              <el-button type="danger" size="small" link>注销</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchEmployeeList"
        @current-change="fetchEmployeeList"
      />
    </div>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增员工" width="400px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="领域">
        <el-select v-model="form.category_id" placeholder="请选择擅长领域">
          <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmployee">确认新增</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const employeeList = ref([]);
const categoryList = ref([]);
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchName = ref('');

// 适配后端的 snake_case
const form = reactive({
  name: '',
  category_id: null,
  phone: '',
});

onMounted(() => {
  fetchEmployeeList();
  fetchCategoryList();
});

// 获取员工列表 (API 2.1)
const fetchEmployeeList = async () => {
  try {
    const res = await axios.get('/api/employee', {
      params: {
        page: currentPage.value,
        page_size: pageSize.value,
        name: searchName.value
      }
    });
    if (res.data.code === 1) {
      // 适配 PageResult.list 和 VO 中的 snake_case 字段
      employeeList.value = res.data.data.list;
      total.value = res.data.data.total;
    }
  } catch (error) {
    ElMessage.error('加载员工列表失败');
  }
};

// 获取课程类型 (API 3.1)
const fetchCategoryList = async () => {
  try {
    const res = await axios.get('/api/category');
    if (res.data.code === 1) {
      categoryList.value = res.data.data;
    }
  } catch (error) {
    ElMessage.error('加载课程类型失败');
  }
};

// 提交新增员工 (API 2.2)
const submitEmployee = async () => {
  try {
    const res = await axios.post('/api/employee/create', form);
    if (res.data.code === 1) {
      ElMessage.success('新增员工成功');
      dialogVisible.value = false;
      resetForm();
      fetchEmployeeList();
    } else {
      ElMessage.error(res.data.msg || '新增失败');
    }
  } catch (error) {
    ElMessage.error('提交失败，请检查数据');
  }
};

// [新增] 删除员工
const deleteEmployee = async (id) => {
  try {
    // 后端 Delete 接口使用 @RequestParam 接收 id
    const res = await axios.delete('/api/employee/delete', {
      params: { id: id }
    });

    if (res.data.code === 1) {
      // 后端成功时返回的数据是 "员工删除成功"
      ElMessage.success(res.data.data || '注销成功');
      fetchEmployeeList(); // 刷新列表
    } else {
      // 后端失败时，错误信息可能在 data 或 msg 中
      // 根据 Controller 代码，失败信息放在了 data 中 ("员工删除失败，请先删除该员工关联的排课信息")
      ElMessage.error(res.data.data || res.data.msg || '注销失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('操作异常');
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchEmployeeList();
};

const resetForm = () => {
  form.name = '';
  form.category_id = null;
  form.phone = '';
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
