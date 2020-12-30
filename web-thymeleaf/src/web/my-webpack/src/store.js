import Vue from 'vue'
import Vuex from 'vuex'
import Cache from './utils/Cache'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        profile: {},
        token: ''
    },
    mutations: {
        setToken(state, token) {
            state.token = token
            Cache.setToken(token)
        },
        setProfile(state, profile) {
            state.profile = profile
        },
        // setNickName(state, nickName) {
        //     state.nickName = nickName
        // },
        logout(state){
            state.profile = undefined
            token = ''
            Cache.cleanToken()
        }
    },
    getters: {
        profile(state) {
            return state.profile
        },
        nickName(state) {
            return state.profile.sub
        }
    }
})