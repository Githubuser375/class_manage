import { createRouter, createWebHashHistory } from 'vue-router';

// 懒加载视图组件
// const Schedule = () => import('../views/Schedule.vue'); // 忽略或删除有问题的 Schedule.vue
const CourseList = () => import('../views/CourseList.vue');
const EmployeeList = () => import('../views/EmployeeList.vue');
const CategoryList = () => import('../views/CategoryList.vue');
const ScheduleTable = () => import('../views/ScheduleTable.vue'); // 确保 ScheduleTable 存在且已定义

const routes = [
  { path: '/', redirect: '/schedule' },

  // 【关键修改】将 /schedule 的组件替换为 ScheduleTable
  {
    path: '/schedule',
    name: 'ScheduleView', // 使用新名称避免冲突，并指向正常工作的组件
    component: ScheduleTable,
    meta: { title: '排课视图' }
  },

  { path: '/course', name: 'CourseList', component: CourseList, meta: { title: '课程管理' } },
  { path: '/employee', name: 'EmployeeList', component: EmployeeList, meta: { title: '员工管理' } },
  { path: '/category', name: 'CategoryList', component: CategoryList, meta: { title: '分类管理' } },

  // 课程专属日程安排路由 (已使用 ScheduleTable，无需修改)
  {
    path: '/schedule/course/:id',
    name: 'CourseSchedule',
    component: ScheduleTable,
    meta: { title: '课程日程安排' }
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

export default router;
