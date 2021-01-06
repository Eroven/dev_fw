import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Login',
      name: 'Login',
      component: () => import('@/views/unauth/Login')
    },
    {
      path: '/Register',
      name: 'Register',
      component: () => import('@/views/unauth/Register')
    }
    , {
      path: '/',
      name: 'Layout',
      component: () => import('@/layout/index.vue'),
      redirect: '/Profile',
      meta: {
        requireAuth: false
      },
      children: [
        {
          path: '/Profile',
          name: 'ProfileIndex',
          component: () => import('@/views/profile/ProfileIndex'),
          meta: {
            requireAuth: true
          }
        },
        {
          path: '/HelloUser',
          name: 'HelloUser',
          component: () => import('@/views/HelloUser'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/OrderManage',
          name: 'OrderManage',
          component: () => import('@/views/OrderManage'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/FileUpload',
          name: 'FileUpload',
          component: () => import('@/views/FileUpload'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/CompleteProfile',
          name: 'CompleteProfile',
          component: () => import('@/views/profile/CompleteProfile'),
          meta: {
            requireAuth: true
          }
        }
      ]
    }
  ]
})
