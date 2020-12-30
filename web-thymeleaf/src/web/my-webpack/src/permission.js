import router from './router'
import store from './store'
import Cache from './utils/Cache'
import {parseToken} from './utils/Parser'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style

router.beforeEach((to, from, next) => {
    NProgress.start()
    let token = store.state.token
    let now = new Date().getTime().toString().substring(0, 10)
    // console.log("permission: " + to.meta.requireAuth)
    if (to.meta.requireAuth) {//要求授权
        let profile
        if (!token) {
            token = Cache.getToken()
            store.commit('setToken',token)
            profile = parseToken(token)
            store.commit('setProfile', profile)
            // store.commit('setNickName', profile.sub)
        } else {
            profile = store.state.profile
        }
        // console.log(profile)
        if (!profile || profile.exp <= now) {
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
