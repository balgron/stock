import Vue from 'vue'
import App from './App'
import { router } from './router'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import '@/plugins/index'
import moment from 'moment'

Vue.use(Element, {
  size: 'mini'
})

Vue.prototype.moment = moment

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
