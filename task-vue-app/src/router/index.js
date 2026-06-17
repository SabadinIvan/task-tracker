import { createRouter, createWebHistory } from 'vue-router';
import RegisterView from '../views/RegisterView.vue';
import LoginView from '@/views/LoginView.vue';
import MainView from '@/views/MainView.vue';
import {useAuthStore} from "@/stores/auth.js";

const routes= [
  {
    path: '/',
    name: 'home',
    component: MainView,
    meta: { requiredAuth: true }
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
    meta: { requiredAuth: false }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiredAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// console.log('routes size -> ' + routes.length);
// routes.forEach(route => {
//   console.log('router ' + route.path + '; ' + route.name + '; ' + route.component + '; ' + route.meta)
// })

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuth = authStore.isAuth;
  if (to.matched.some(record => record.meta.requiredAuth)) {
    if (!isAuth) {
      next({name: 'login' });
    } else {
      next();
    }
  } else if ((to.name === 'login' || to.name === 'register') && isAuth) {
    next({ name: 'home' });
  } else {
    next();
  }
})

export default router
