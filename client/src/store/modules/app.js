const app = {
  state: {
    closeNav: false,
    title: '股票分析'
  },
  mutations: {
    SET_NAV_CLOSE: (state, close) => {
      state.closeNav = close
    }
  },
  actions: {}
}

export default app
