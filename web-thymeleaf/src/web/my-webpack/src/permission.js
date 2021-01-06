import router from './router'
import store from './store'
import Cache from './utils/Cache'
import {parseToken} from './utils/Parser'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style

router.beforeEach((to, from, next) => {
    NProgress.start()
    let token = store.state.token
    let profile = store.state.profile
    let now = new Date().getTime().toString().substring(0, 10)
    if (to.meta.requireAuth) {//要求授权`
        let profile
        if (!token) {
            token = Cache.getToken()
            if (!token || null == token || "null" == token) {
                next({path: '/Login'})
                return
            }
            profile = parseToken(token)
            store.commit('setToken',token)
            store.commit('setProfile', profile)
        } else {
            profile = store.state.profile
        }
        if (!profile || profile.expiration <= now) {
            next({path: '/Login'})
        } else {
            next()
        }
    } else {
        next()
    }
})

router.afterEach(() => {
	NProgress.done()
})
