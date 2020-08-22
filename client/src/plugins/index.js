import Vue from 'vue'
import Table from '@/component/table'
import StockSelect from '@/component/stock-select'
import StockChart from '@/component/stock-chart'
import StockOperate from '@/component/stock-operate'
import StrategySelect from '@/component/strategy-select'
import HyperParams from '@/component/hyper-params'

Vue.component('stock-chart', StockChart)
Vue.component('s-table', Table)
Vue.component('stock-select', StockSelect)
Vue.component('stock-operate', StockOperate)
Vue.component('strategy-select', StrategySelect)
Vue.component('hyper-params', HyperParams)
