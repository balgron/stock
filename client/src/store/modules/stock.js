import {stockList} from '@/api/stock'

const stock = {
  state: {
    stocks: []
  },
  mutations: {
    SET_STOCK: (state, stocks) => {
      state.stocks = stocks
    }
  },
  actions: {
    getStock ({ commit }) {
      stockList({code: ''})
        .then(res => {
          commit('SET_STOCK', res)
        })
    }
  }
}

export default stock
