<template>
  <el-config-provider :locale="zhCn">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>📅 课程日历</span>

          <div>
            <el-select
              v-model="currentCourseId"
              placeholder="选择课程"
              style="width: 200px; margin-right: 10px"
              filterable
              @change="fetchSchedules"
            >
              <el-option label="全部课程 (全局)" :value="null" />
              <el-option
                v-for="c in courseList"
                :key="c.id"
                :label="c.name"
                :value="c.id"
              />
            </el-select>

            <el-date-picker
              v-model="currentDate"
              type="date"
              value-format="YYYY-MM-DD"
              @change="fetchSchedules"
            />
          </div>
        </div>
      </template>

      <el-table
        :data="classroomList"
        border
        style="width: 100%"
        :span-method="objectSpanMethod"
        v-loading="loading"
        :cell-style="{ padding: '2px' }"
      >
        <el-table-column prop="code" label="教室" width="120" fixed />

        <el-table-column
          v-for="p in 9"
          :key="p"
          width="120"
          align="center"
        >
          <template #header>
            <div class="column-header">
              <span>第 {{ p }} 节</span>
              <span class="sub-time">{{ PERIOD_TIME[p] }}</span>
            </div>
          </template>

          <template #default="{ row }">
            <div class="matrix-cell" @click="handleMatrixCellClick(p, row)">

              <template v-if="getSchedule(p, row.id)">

                <template v-if="!currentCourseId || getSchedule(p, row.id).course_id === currentCourseId">
                  <div class="course-btn-wrapper" @click.stop="openDetailDialog(getSchedule(p, row.id))">
                    <el-button
 round
  class="course-btn"
  :style="{
    backgroundColor: getCourseColor(getSchedule(p, row.id).course_id),
    borderColor: getCourseColor(getSchedule(p, row.id).course_id),
    color: '#fff'
  }"
>
                      <div class="btn-text">
                        <span class="course-name">
                          {{ getSchedule(p, row.id).course_name }}
                        </span>
                        <span class="teacher-name">
                          {{ getSchedule(p, row.id).teacher_names.join('、') }}
                        </span>
                      </div>
                    </el-button>
                  </div>
                </template>

                <template v-else>
                  <div class="occupied-block">
                    <span>已占用</span>
                    <span class="occupied-sub">(其他课程)</span>
                  </div>
                </template>

              </template>

              <div v-else class="empty-cell-content">
                <span class="plus-sign">＋</span>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增排课" width="520px">
      <el-form label-width="90px">
        <el-form-item label="课程">
          <el-select
            v-model="form.course_id"
            placeholder="请选择课程"
            filterable
            :disabled="!!currentCourseId"
            @change="() => { form.end_period = null; availableClassrooms = [] }"
          >
            <el-option v-for="c in courseList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="日期">
          <el-input :value="currentDate" disabled />
        </el-form-item>

        <el-form-item label="节次">
          <el-select v-model="form.start_period" placeholder="开始" style="width: 120px" @change="onStartPeriodChange">
            <el-option v-for="p in 9" :key="p" :label="`第 ${p} 节`" :value="p" />
          </el-select>
          <span style="margin: 0 8px">至</span>
          <el-select
            v-model="form.end_period"
            placeholder="结束"
            style="width: 120px"
            :disabled="!form.start_period"
            @change="fetchAvailableResources"
          >
            <el-option v-for="p in endPeriodOptions" :key="p" :label="`第 ${p} 节`" :value="p" />
          </el-select>
        </el-form-item>

        <el-form-item label="教室">
          <el-select v-model="form.classroom_id" placeholder="选择教室">
            <el-option v-for="r in availableClassrooms" :key="r.id" :label="r.code" :value="r.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="教师">
          <el-select v-model="form.employee_ids" multiple placeholder="选择教师" style="width: 100%">
            <el-option v-for="e in availableEmployees" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitSchedule">确认排课</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="课程详情" width="500px">
      <el-form label-width="90px">
        <el-form-item label="课程名称">
          <el-tag effect="dark">{{ currentDetail.course_name }}</el-tag>
        </el-form-item>
        <el-form-item label="时间地点">
          <el-input
            :value="`${currentDetail.date} (第 ${currentDetail.start_period}-${currentDetail.end_period} 节) @ ${currentDetail.classroom_code}`"
            disabled
          />
        </el-form-item>
        <el-form-item label="当前教师">
           <el-tag v-for="name in currentDetail.teacher_names" :key="name" style="margin-right: 5px">
             {{ name }}
           </el-tag>
        </el-form-item>
                <el-form-item label="课程费用">
          <el-tag effect="light">{{ currentDetail.course_fee }}元/节</el-tag>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer-between">
          <el-button type="warning" @click="openEmployeeModifyDialog">
            <el-icon style="margin-right: 5px"><User /></el-icon> 修改授课教师
          </el-button>

          <div>
            <el-button @click="detailDialogVisible = false">关闭</el-button>
            <el-popconfirm
              title="确定要删除这条排课记录吗？"
              confirm-button-text="删除"
              cancel-button-text="取消"
              @confirm="handleDeleteSchedule"
            >
              <template #reference>
                <el-button type="danger">删除排课</el-button>
              </template>
            </el-popconfirm>
          </div>
        </div>
      </template>
    </el-dialog>

<el-dialog v-model="employeeDialogVisible" title="分配教师" width="600px" append-to-body>

      <div class="custom-transfer">

        <div class="list-panel">
          <div class="panel-header">可用教师 ({{ unselectedList.length }})</div>
          <div class="panel-content">
            <template v-if="unselectedList.length > 0">
              <div
                v-for="item in unselectedList"
                :key="item.key"
                class="list-item"
                @click="moveToRight(item)"
              >
                <span>{{ item.label }}</span>
                <el-icon><ArrowRight /></el-icon>
              </div>
            </template>
            <div v-else class="empty-text">无可用教师</div>
          </div>
        </div>

        <div class="transfer-arrow">
          <el-icon><Switch /></el-icon>
        </div>

        <div class="list-panel selected-panel">
          <div class="panel-header">已选教师 ({{ selectedList.length }})</div>
          <div class="panel-content">
             <template v-if="selectedList.length > 0">
              <div
                v-for="item in selectedList"
                :key="item.key"
                class="list-item"
                @click="moveToLeft(item)"
              >
                <el-icon><ArrowLeft /></el-icon>
                <span>{{ item.label }}</span>
              </div>
             </template>
             <div v-else class="empty-text">未选择</div>
          </div>
        </div>

      </div>

      <div style="margin-top: 10px; color: #909399; font-size: 12px; text-align: center;">
        点击教师姓名即可直接移动。
      </div>

      <template #footer>
        <el-button @click="employeeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmployeeChange" :loading="modifying">确认修改</el-button>
      </template>
    </el-dialog>

  </el-config-provider>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { User, ArrowRight, ArrowLeft, Switch } from '@element-plus/icons-vue'

const COURSE_COLORS = [
  '#409EFF', // 默认蓝
  '#67C23A', // 成功绿
  '#E6A23C', // 警告橙
  '#F56C6C', // 危险红
  '#909399', // 灰色
  '#626aef', // 紫色
  '#009688', // 青色
  '#e91e63', // 粉色
  '#795548', // 棕色
  '#3f51b5'  // 靛青
]

// 根据课程ID计算颜色
function getCourseColor(courseId) {
  if (!courseId) return '#409EFF'
  // 使用取模运算，保证颜色在列表内循环
  const index = courseId % COURSE_COLORS.length
  return COURSE_COLORS[index]
}

/* -------------------- 基础状态 -------------------- */
const route = useRoute()
const loading = ref(false)

const courseList = ref([])
const classroomList = ref([])
const currentCourseId = ref(route.params.id ? Number(route.params.id) : null)
const currentDate = ref(new Date().toISOString().slice(0, 10))

// 新增排课弹窗状态
const dialogVisible = ref(false)
const availableClassrooms = ref([])
const availableEmployees = ref([])

// [新增] 详情与修改弹窗状态
const detailDialogVisible = ref(false)
const employeeDialogVisible = ref(false)
const currentDetail = ref({})
const modifying = ref(false)
// 穿梭框数据
const transferData = ref([])       // 所有可选数据 { key, label }
const selectedEmployeeIds = ref([]) // 已选 ID 列表

/* -------------------- 常量与映射 -------------------- */
const PERIOD_TIME = {
  1: '08:00-09:00', 2: '09:00-10:00', 3: '10:00-11:00',
  4: '13:00-14:00', 5: '14:00-15:00', 6: '15:00-16:00',
  7: '18:00-19:00', 8: '19:00-20:00', 9: '20:00-21:00'
}
const PERIOD_GROUP = { 1:1, 2:1, 3:1, 4:2, 5:2, 6:2, 7:3, 8:3, 9:3 }
const scheduleMap = ref({})

/* -------------------- 表单状态 -------------------- */
const form = reactive({
  course_id: null, date: '', start_period: null, end_period: null, classroom_id: null, employee_ids: []
})

const endPeriodOptions = computed(() => {
  if (!form.start_period) return []
  const group = PERIOD_GROUP[form.start_period]
  return Object.keys(PERIOD_GROUP).map(Number).filter(p => p >= form.start_period && PERIOD_GROUP[p] === group)
})

// 计算属性：左侧列表 (所有数据 - 已选ID)
const unselectedList = computed(() => {
  return transferData.value.filter(item => !selectedEmployeeIds.value.includes(item.key))
})

// 计算属性：右侧列表 (所有数据 中 ID 在 已选列表 里的)
const selectedList = computed(() => {
  return transferData.value.filter(item => selectedEmployeeIds.value.includes(item.key))
})

// 动作：移动到右边 (添加 ID)
function moveToRight(item) {
  selectedEmployeeIds.value.push(item.key)
}

// 动作：移动到左边 (移除 ID)
function moveToLeft(item) {
  const index = selectedEmployeeIds.value.indexOf(item.key)
  if (index > -1) {
    selectedEmployeeIds.value.splice(index, 1)
  }
}

/* -------------------- 辅助函数 -------------------- */
function getSchedule(period, classroomId) {
  if (!classroomId) return null
  return scheduleMap.value[`${period}-${classroomId}`]
}

const objectSpanMethod = ({ row, column, rowIndex, columnIndex }) => {
  if (columnIndex === 0) return { rowspan: 1, colspan: 1 }
  const currentPeriod = columnIndex
  const schedule = getSchedule(currentPeriod, row.id)
  if (schedule) {
    if (schedule.start_period === currentPeriod) {
      return { rowspan: 1, colspan: schedule.end_period - schedule.start_period + 1 }
    } else {
      return { rowspan: 0, colspan: 0 }
    }
  }
  return { rowspan: 1, colspan: 1 }
}

/* -------------------- 生命周期 -------------------- */
onMounted(async () => {
  await fetchCourseList()
  await fetchClassroomList()
  fetchSchedules()
})

/* -------------------- 核心 API -------------------- */

async function fetchCourseList() {
  const res = await axios.get('/api/class', { params: { page: 1, page_size: 100 } })
  courseList.value = res.data.data.list
}

async function fetchClassroomList() {
  const res = await axios.get('/api/classroom')
  if (res.data.code === 1) classroomList.value = res.data.data || []
}

// 获取排课数据（并发获取所有课程）
async function fetchSchedules() {
  loading.value = true
  let rawSchedules = []
  const params = { start_date: currentDate.value, end_date: currentDate.value }

  try {
    if (courseList.value.length === 0) await fetchCourseList()

    const requestPromises = courseList.value.map(course =>
      axios.get(`/api/class/${course.id}/schedules`, { params })
        .then(res => {
          const list = res.data.data || []
          return list.map(item => ({
            ...item,
            course_name: course.name,
            course_id: course.id
          }))
        })
        .catch(() => [])
    )

    const results = await Promise.all(requestPromises)
    rawSchedules = results.flat()

    const map = {}
    const codeToIdMap = {}
    if (classroomList.value) classroomList.value.forEach(cls => codeToIdMap[cls.code] = cls.id)

    rawSchedules.forEach(s => {
      const sCode = s.classroom_code || s.classroomCode
      const sId = s.classroom_id || codeToIdMap[sCode]
      if (sId) {
        const start = s.start_period || s.startPeriod
        const end = s.end_period || s.endPeriod
        for (let p = start; p <= end; p++) {
          map[`${p}-${sId}`] = {
            ...s,
            teacher_names: s.teacher_names || s.teacherNames,
            teacher_ids: s.teacherIds || s.teacher_ids,
            classroom_code: sCode
          }
        }
      }
    })
    scheduleMap.value = map
  } catch (error) {
    console.error(error)
    ElMessage.error('加载排课数据失败')
  } finally {
    loading.value = false
  }
}

// 获取可用资源 (用于新增排课冲突检测)
async function fetchAvailableResources() {
  if (!form.start_period || !form.end_period) return
  let categoryId = null
  if (form.course_id) {
    const c = courseList.value.find(i => i.id === form.course_id)
    if (c) categoryId = c.category_id
  }
  try {
    const res = await axios.post('/api/resource/available', {
      date: form.date, start_period: form.start_period, end_period: form.end_period, category_id: categoryId
    })
    availableClassrooms.value = res.data.data.available_classrooms || []
    availableEmployees.value = res.data.data.available_employees || []

    if (form.classroom_id && !availableClassrooms.value.some(r => r.id === form.classroom_id)) {
      ElMessage.warning('该时段教室不可用，请重新选择！')
      form.classroom_id = null
    }
  } catch (error) { console.error(error) }
}

async function submitSchedule() {
  const response = await axios.post('/api/schedule/add', {
    course_id: form.course_id,
    date: form.date,
    start_period: form.start_period,
    end_period: form.end_period,
    classroom_id: form.classroom_id,
    employee_ids: form.employee_ids
  }).catch(error => {
    // 网络错误或HTTP状态码错误
    ElMessage.error('请求失败，请检查网络')
    throw error // 不再继续执行
  })

  const result = response.data

  if (result.code === 1) {
    ElMessage.success('排课成功')
    dialogVisible.value = false
    fetchSchedules()
  } else {
    // 直接显示后端返回的错误信息
    ElMessage.error(result.msg)
  }
}
/* -------------------- [新增] 详情与操作逻辑 -------------------- */

// 1. 点击格子打开详情
// 1. 点击格子打开详情
function openDetailDialog(schedule) {
  currentDetail.value = {
    ...schedule,
    // 直接从schedule中获取actual_fee，如果不存在则查找courseList
    course_fee: schedule.actual_fee || schedule.actualFee ||
                getCourseActualFee(schedule.course_id) || '未设置'
  };
  detailDialogVisible.value = true;
}

// 辅助函数：从courseList获取课程实际费用
function getCourseActualFee(courseId) {
  if (!courseId) return null;
  const course = courseList.value.find(c => c.id === courseId);
  return course ? course.actual_fee : null;
}

// 2. 删除排课
async function handleDeleteSchedule() {
  try {
    const res = await axios.delete('/api/schedule/delete', {
      params: { scheduleId: currentDetail.value.id }
    })
    if (res.data.code === 1) {
      ElMessage.success('删除排课成功')
      detailDialogVisible.value = false
      fetchSchedules()
    } else {
      ElMessage.error(res.data.msg || '删除失败')
    }
  } catch (error) {
    ElMessage.error('操作异常')
  }
}

// 3. 打开修改教师弹窗
async function openEmployeeModifyDialog() {
  const schedule = currentDetail.value
  let categoryId = null

  // 获取课程分类ID (为了筛选该领域的老师)
  const course = courseList.value.find(c => c.name === schedule.course_name)
  if (course) categoryId = course.category_id

  try {
    // 3.1 获取该时段所有可用教师
    const res = await axios.post('/api/resource/available', {
      date: schedule.date,
      start_period: schedule.start_period,
      end_period: schedule.end_period,
      category_id: categoryId
    })

    const availableList = res.data.data.available_employees || []

    // 3.2 构造数据源
    const data = []

    // 添加后端返回的“可用教师”
    availableList.forEach(emp => {
      data.push({ key: emp.id, label: emp.name })
    })

    // 3.3 回显当前已选教师
    // 注意：我们在 fetchSchedules 里映射的是 teacher_ids
    if (schedule.teacher_ids && schedule.teacher_ids.length > 0) {
       selectedEmployeeIds.value = [...schedule.teacher_ids]

       // [关键补全逻辑]
       // 如果当前已选的老师因为“时间冲突”不在 availableList 里，
       // 我们必须手动把他们加到 data 数据源里，否则右侧列表不知道 id 对应的名字是谁。
       schedule.teacher_ids.forEach((id, index) => {
          // 检查 data 里是否已经有了这个人
          const exists = data.some(item => item.key === id)
          if (!exists) {
             // 如果没有，手动补全 (利用 teacher_names 数组，假设顺序一致)
             const name = schedule.teacher_names[index] || '未知教师'
             data.push({ key: id, label: name })
          }
       })
    } else {
       selectedEmployeeIds.value = []
    }

    // 赋值给穿梭框数据源
    transferData.value = data
    employeeDialogVisible.value = true

  } catch (e) {
    console.error(e)
    ElMessage.error('无法加载可用教师资源')
  }
}

// 4. 提交教师变更
async function submitEmployeeChange() {
  if (selectedEmployeeIds.value.length === 0) {
    ElMessage.warning('请至少选择一名教师')
    return
  }

  modifying.value = true
  try {
    // 接口规范: scheduleId 为 URL 参数, employeeIds 为 Body
    await axios.post(
      `/api/schedule/assign-employees?scheduleId=${currentDetail.value.id}`,
      selectedEmployeeIds.value
    )

    ElMessage.success('教师分配更新成功')
    employeeDialogVisible.value = false
    detailDialogVisible.value = false
    fetchSchedules()
  } catch (error) {
    ElMessage.error('更新失败')
  } finally {
    modifying.value = false
  }
}

/* -------------------- 矩阵点击交互 -------------------- */
function handleMatrixCellClick(period, rowClassroom) {
  // 如果有课，不触发新增，点击事件已由内部按钮的 stopPropagation 接管
  if (getSchedule(period, rowClassroom.id)) return

  Object.assign(form, {
    course_id: currentCourseId.value ? currentCourseId.value : null,
    date: currentDate.value,
    start_period: period,
    end_period: null,
    classroom_id: rowClassroom.id,
    employee_ids: []
  })
  availableClassrooms.value = [rowClassroom]
  availableEmployees.value = []
  dialogVisible.value = true
}

function onStartPeriodChange() {
  form.end_period = null
  availableEmployees.value = []
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.column-header {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}
.sub-time {
  font-size: 11px;
  color: #909399;
  font-weight: normal;
}
.matrix-cell {
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 2px;
}
.course-btn-wrapper {
  width: 100%;
  height: 100%;
}
.course-btn {
  width: 100%;
  height: 100%;
  padding: 0 4px;
  border-radius: 6px;
  border: none;
}
.btn-text {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  overflow: hidden;
}
.course-name {
  font-size: 12px;
  line-height: 1.2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
.teacher-name {
  font-size: 10px;
  opacity: 0.9;
  transform: scale(0.9);
}
.occupied-block {
  width: 100%;
  height: 100%;
  background-color: #f0f2f5;
  color: #c0c4cc;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  border: 1px dashed #dcdfe6;
  cursor: not-allowed;
  user-select: none;
}
.occupied-sub {
  font-size: 10px;
  transform: scale(0.9);
}
.empty-cell-content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}
.matrix-cell:hover .empty-cell-content {
  background-color: #f0f9eb;
}
.plus-sign {
  color: transparent;
  font-weight: bold;
}
.matrix-cell:hover .plus-sign {
  color: #67c23a;
}
.dialog-footer-between {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
/* [新增] 强制修复穿梭框样式 */
.transfer-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 使用 :deep() 穿透 Element Plus 的内部样式 */
:deep(.el-transfer) {
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.el-transfer-panel) {
  width: 250px; /* 强制面板宽度，防止挤压 */
  text-align: left; /* 保证内部文字居左 */
  margin: 0; /* 清除可能导致换行的外边距 */
}

:deep(.el-transfer__buttons) {
  padding: 0 20px; /* 保证中间按钮有空间 */
  display: flex;
  flex-direction: column; /* 按钮垂直排列 */
  justify-content: center;
}

/* 自定义穿梭框容器 */
.custom-transfer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 300px; /* 固定高度 */
}

/* 列表面板 */
.list-panel {
  width: 45%;
  height: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: #fff;
}

/* 已选面板稍微变色区分 */
.selected-panel {
  border-color: #409eff;
  background-color: #f0f9eb;
}

.panel-header {
  padding: 10px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
  font-weight: bold;
  font-size: 14px;
  color: #606266;
}
.selected-panel .panel-header {
  background-color: #ecf5ff;
  color: #409eff;
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 5px;
}

/* 列表项 */
.list-item {
  padding: 8px 10px;
  margin-bottom: 4px;
  cursor: pointer;
  border-radius: 4px;
  display: flex;
  justify-content: space-between; /* 图标和文字两端对齐 */
  align-items: center;
  font-size: 13px;
  transition: all 0.2s;
}

.list-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
  transform: translateX(2px); /* 悬停微动特效 */
}

/* 右侧列表悬停样式 */
.selected-panel .list-item:hover {
  background-color: #fef0f0;
  color: #f56c6c;
  transform: translateX(-2px);
}

.empty-text {
  text-align: center;
  color: #909399;
  margin-top: 50px;
  font-size: 12px;
}

.transfer-arrow {
  color: #909399;
  font-size: 20px;
}
</style>
