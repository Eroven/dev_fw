import Vue from 'vue'
import Vuex from 'vuex'
import Cache from './utils/Cache'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        profile: undefined,
        token: undefined
    },
    mutations: {
        setToken(state, token) {
            state.token = token
        },
        setProfile(state, profile) {
            state.profile = profile
        },
        // setNickName(state, nickName) {
        //     state.nickName = nickName
        // },
        logout(state){
            state.profile = ''
            state.token = ''
            Cache.cleanToken()
        }
    },
    getters: {
        profile(state) {
            return state.profile
        },
        nickName(state) {
            return state.profile.userInfo ? state.profile.userInfo.nickName : ''
        }
    }
})