import Vue from 'vue'
import Router from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {commonRouter, appRouter} from './router'

NProgress.configure({showSpinner: false}) // NProgress Configuration
export const routers = [...commonRouter, ...appRouter]
export const router = new Router({
  routes: routers
})

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  next()
  NProgress.done()
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

Vue.use(Router)

export default {
  router
}
