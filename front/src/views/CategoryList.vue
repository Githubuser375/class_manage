<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>分类管理</span>
        <el-button type="primary" @click="dialogVisible = true">新增课程类型</el-button>
      </div>
    </template>

    <el-table :data="categoryList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="类型名称" width="200" />
      <el-table-column prop="base_fee" label="基础费用" />
      <el-table-column label="操作" width="120">
  <template #default="scope">
    <el-popconfirm
      title="确定要删除该分类吗？"
      confirm-button-text="确定"
      cancel-button-text="取消"
      @confirm="deleteCategory(scope.row.id)"
    >
      <template #reference>
        <el-button type="danger" link size="small">删除</el-button>
      </template>
    </el-popconfirm>
  </template>
</el-table-column>
    </el-table>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增课程类型" width="400px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="类型名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="基础费用">
        <el-input-number v-model="form.base_fee" :min="0" :precision="2" controls-position="right" style="width: 100%;" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCategory">确认新增</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const categoryList = ref([]);
const dialogVisible = ref(false);

const form = reactive({
  name: '',
  base_fee: 0,
});

onMounted(() => {
  fetchCategoryList();
});

// 获取课程类型列表 (API 3.1)
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

// 提交新增课程类型 (API 3.2)
const submitCategory = async () => {
  try {
    const res = await axios.post('/api/category/create', form);
    if (res.data.code === 1) {
      ElMessage.success('新增类型成功');
      dialogVisible.value = false;
      resetForm();
      fetchCategoryList();
    } else {
      ElMessage.error(res.data.msg || '新增失败');
    }
  } catch (error) {
    ElMessage.error('提交失败，请检查数据');
  }
};

const resetForm = () => {
  form.name = '';
  form.base_fee = 0;
};

const deleteCategory = async (id) => {
  try {
    const res = await axios.delete('/api/category/delete', {
      params: { id: id }
    });

    if (res.data.code === 1) {
      ElMessage.success('删除成功');
      fetchCategoryList(); // 刷新列表
    } else {
      // 后端逻辑：如果有由于外键约束导致失败，错误信息在 data 中
      ElMessage.error(res.data.data || res.data.msg || '删除失败，请检查该分类下是否还有课程或员工');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('操作异常');
  }
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
