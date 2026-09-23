import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Home/Layout.vue'
import Login from '../views/Login.vue'
import Index from '../views/Home/index.vue'
import courses from '../views/courese/courses.vue'
import about from '../views/about/about.vue'
import studentSpace from '../views/student/space.vue'
import employeeSpace from '../views/employee/space.vue'
import coursesDetail from '../views/courese/courses_detail.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Layout',
      component: Layout,
      redirect: '/index',
      children: [
        { path: '/index', name: 'index', component: Index },
        { path: '/courses', name: 'courses', component: courses },
        { path: '/about', name: 'about', component: about },
        { path: '/courses/:id', name: 'coursesDetail', component: coursesDetail },
        { path: '/courses/:id', name: 'coursesDetail', component: coursesDetail },
        { path: '/student/space', name: 'studentSpace', component: studentSpace },
      ],
    },
    { path: '/login', name: 'login', component: Login },

    { path: '/employee/space', name: 'employeeSpace', component: employeeSpace },
  ],
})

export default router
