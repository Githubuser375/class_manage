<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>课程管理</span>
        <div>
          <el-input v-model="searchName" placeholder="按课程名称搜索" style="width: 200px; margin-right: 10px;" @change="handleSearch" clearable />
          <el-button type="primary" @click="dialogVisible = true">新增课程</el-button>
        </div>
      </div>
    </template>

    <el-table :data="courseList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="课程名称" width="200" />
      <el-table-column prop="category_name" label="课程类型" width="150" />
      <el-table-column prop="description" label="描述" />

<el-table-column label="操作" width="200" fixed="right">
  <template #default="scope">
    <el-button size="small" type="primary" @click="goToSchedule(scope.row.id)">日程安排</el-button>

    <el-popconfirm
      title="确定要删除该课程吗？"
      confirm-button-text="确定"
      cancel-button-text="取消"
      @confirm="deleteCourse(scope.row.id)"
    >
      <template #reference>
        <el-button type="danger" link size="small" style="margin-left: 10px">删除</el-button>
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
        @size-change="fetchCourseList"
        @current-change="fetchCourseList"
      />
    </div>
  </el-card>

  <el-dialog v-model="dialogVisible" title="新增课程" width="500px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="课程名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="课程类型">
        <el-select v-model="form.category_id" placeholder="请选择课程类型">
          <el-option v-for="cat in categoryList" :key="cat.id" :label="cat.name" :value="cat.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="课程描述">
        <el-input v-model="form.description" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCourse">确认新增</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router'; // 【新增】导入 useRouter

const router = useRouter(); // 【新增】初始化 router 实例

const courseList = ref([]);
const categoryList = ref([]);
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const searchName = ref('');

const form = reactive({
  name: '',
  category_id: null,
  description: '',
});

onMounted(() => {
  fetchCourseList();
  fetchCategoryList();
});

// 【新增】跳转到排课页面
const goToSchedule = (courseId) => {
    // 跳转到 /schedule/course/123 这样的路径
    router.push({ name: 'CourseSchedule', params: { id: courseId } });
};

// 获取课程列表 (API 1.1)
const fetchCourseList = async () => {
  try {
    const res = await axios.get('/api/class', {
      params: {
        page: currentPage.value,
        page_size: pageSize.value,
        name: searchName.value
      }
    });
    if (res.data.code === 1) {
      courseList.value = res.data.data.list;
      total.value = res.data.data.total;
    }
  } catch (error) {
    ElMessage.error('加载课程列表失败');
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

// 提交新增课程 (API 1.2)
const submitCourse = async () => {
  try {
    const res = await axios.post('/api/class/create', form);
    if (res.data.code === 1) {
      ElMessage.success('新增课程成功');
      dialogVisible.value = false;
      resetForm();
      fetchCourseList();
    } else {
      ElMessage.error(res.data.msg || '新增失败');
    }
  } catch (error) {
    ElMessage.error('提交失败，请检查数据');
  }
};

const deleteCourse = async (id) => {
  try {
    const res = await axios.delete('/api/class/delete', {
      params: { id: id }
    });

    if (res.data.code === 1) {
      ElMessage.success('删除成功');
      fetchCourseList(); // 刷新列表
    } else {
      // 后端逻辑：如果有关联排课，failure.setData("课程删除失败...")
      ElMessage.error(res.data.data || res.data.msg || '删除失败，请先删除该课程的排课信息');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('操作异常');
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchCourseList();
};

const resetForm = () => {
  form.name = '';
  form.category_id = null;
  form.description = '';
};
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
