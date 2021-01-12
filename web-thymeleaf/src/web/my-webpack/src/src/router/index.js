import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Login',
      component: () => import('@/views/unauth/Login')
    },
    {
      path: '/Register',
      component: () => import('@/views/unauth/Register')
    }
    , {
      path: '/',
      component: () => import('@/layout/index.vue'),
      redirect: '/Profile',
      meta: {
        requireAuth: false
      },
      children: [
        {
          path: '/Profile',
          component: () => import('@/views/profile/ProfileIndex'),
          redirect: '/Profile/EditProfile',
          meta: {
            requireAuth: true
          },
          children: [
            {
              path: 'EditProfile',
              component: () => import('@/views/profile/component/EditProfile'),
              name: 'EditProfile',
              meta: {
                requireAuth: true
              }
            },{
              path: 'EditProfilePhoto',
              name: 'EditProfilePhoto',
              component: () => import('@/views/profile/component/EditProfilePhoto'),
              meta: {
                requireAuth: true
              }
            }
          ]
        },
        {
          path: '/HelloUser',
          component: () => import('@/views/HelloUser'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/OrderManage',
          component: () => import('@/views/OrderManage'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/FileUpload',
          component: () => import('@/views/FileUpload'),
          meta: {
            requireAuth: true
          }
        },{
          path: '/CompleteProfile',
          component: () => import('@/views/profile/CompleteProfile'),
          meta: {
            requireAuth: true
          }
        }
      ]
    }
  ]
})
