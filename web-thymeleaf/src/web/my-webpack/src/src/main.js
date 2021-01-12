import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Axios from 'axios'

import '@/permission'

Vue.config.productionTip = false

Vue.use(ElementUI)

Axios.defaults.baseURL = '/api' //统一加个前缀
Axios.defaults.headers.common['Content-Type'] = 'applicaiton/json'
Axios.interceptors.request.use(
	config => {
		if (store.state.token) {
			config.headers['Authorization'] = 'Bearer ' + store.state.token
		}
		return config;
	},
	err => {
		return Promise.reject(err);
	}
);
Vue.prototype.$axios = Axios

/* eslint-disable no-new */
const app = new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
