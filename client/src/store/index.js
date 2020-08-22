import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import stock from './modules/stock'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    stock
  },
  getters
})

export default store
