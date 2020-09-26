<template>
  <tools v-model="submitForm" :suggest="lastSuggest.sale ? '卖' : (lastSuggest.nothing ? '不处理': '买')" @next="next">
    <el-row>
      <el-col>
        <el-form inline>
          <el-form-item label="股票">
            <stock-select v-model="queryForm.stockCode"></stock-select>
          </el-form-item>
          <el-form-item label="开始时间">
            <el-date-picker
              :value="queryForm.startDate"
              type="date"
              value-format="yyyy-MM-dd"
              :clearable="false"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="策略">
            <strategy-select v-model="queryForm.strategyCode"></strategy-select>
          </el-form-item>
          <el-form-item label="初始金额">
            <el-input-number v-model="queryForm.initMoney" :step="50000" :min="100000"></el-input-number>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">开始模拟</el-button>
            <el-button type="primary" @click="showChart = !showChart">{{showChart ? '隐藏图表' : '展示图表'}}</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col>
        <hyper-params v-model="queryForm.hyperParams" :code="queryForm.strategyCode"></hyper-params>
      </el-col>
      <el-col v-if="showChart">
        <stock-chart :chart-data="stockHistory"></stock-chart>
<!--        <chart :history-data="stockHistory" :operate-data="resultList"></chart>-->
      </el-col>
      <el-col>
        <s-table :table-data="dataList">
          <el-table-column label="日期" prop="date"></el-table-column>
          <el-table-column label="持有资金" prop="holdMoney"></el-table-column>
          <el-table-column label="持有股票" prop="holdVolume"></el-table-column>
          <el-table-column label="股票每股价格" prop="originMoney"></el-table-column>
          <el-table-column label="机器建议" prop="machine">
            <template slot-scope="{ row }">
              <span v-if="row.machine.sale">卖</span>
              <span v-else-if="row.machine.nothing">-</span>
              <span v-else>买</span>
            </template>
          </el-table-column>
          <el-table-column label="实际操作" prop="actual">
            <template slot-scope="{ row }">
              <span v-if="row.operate === 'sale'">卖</span>
              <span v-else-if="row.operate === 'nothing'">-</span>
              <span v-else>买</span>
            </template>
          </el-table-column>
          <el-table-column label="金额" prop="money"></el-table-column>
          <el-table-column label="数量" prop="volume"></el-table-column>
        </s-table>
      </el-col>
    </el-row>
  </tools>
</template>

<script>
import {simulation, stockHistory, suggestLast} from '@/api/stock'
import Chart from './components/chart'
import Tools from './components/tools'
export default {
  components: {
    Chart, Tools
  },
  data: () => ({
    queryForm: {
      stockCode: '',
      startDate: '',
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    },
    currDayIndex: -1,
    show: false,
    showChart: false,
    submitForm: {
      holdMoney: 0,
      holdVolume: 0,
      date: '',
      originMoney: 0,
      price: 0,
      volume: 0,
      operate: 'nothing'
    },
    tradeDates: [],
    stockHistory: [],
    lastSuggest: {},
    resultList: []
  }),
  mounted () {
    this.queryForm = {
      stockCode: 'sz000001',
      startDate: this.moment().add(-1, 'year').format('YYYY-MM-DD'),
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    }
  },
  methods: {
    handleQuery () {
      this.resultList = []
      this.lastSuggest = {}
      this.getHistory(this.queryForm.startDate)
        .then(() => {
          this.getTsDate()
            .then(() => {
              this.currDayIndex = this.tradeDates.indexOf(this.queryForm.startDate)
              let d = this.moment(this.queryForm.startDate)
              while (this.currDayIndex === -1) {
                d = d.add(1, 'day')
                this.currDayIndex = this.tradeDates.indexOf(d.format('YYYY-MM-DD'))
              }
              this.getSuggest()
              this.submitForm = {
                ...this.queryForm,
                holdMoney: this.queryForm.initMoney,
                holdVolume: 0,
                date: this.tradeDates[this.currDayIndex],
                originMoney: 0,
                price: this.stockHistory[this.stockHistory.length - 1].close || 0,
                volume: 0,
                operate: 'nothing'
              }
              this.show = true
            })
        })
    },
    async getTsDate () {
      const endDate = this.moment().add(-1, 'day').format('YYYY-MM-DD')
      const startDate = this.moment(endDate).add(-3, 'year').format('YYYY-MM-DD')
      const res = await this.getHistoryData(startDate, endDate)
      if (res) {
        this.tradeDates = res.map(e => e.day)
      }
    },
    next () {
      this.getSimulation()
        .then(() => {
          this.currDayIndex++
          if (this.currDayIndex >= this.tradeDates.length) {
            return
          }
          this.getHistory(this.tradeDates[this.currDayIndex])
            .then(() => {
              const last = this.resultList[this.resultList.length - 1]
              this.submitForm = {
                ...this.queryForm,
                holdMoney: last.holdMoney,
                holdVolume: last.holdVolume || 0,
                date: this.tradeDates[this.currDayIndex],
                originMoney: last.originMoney,
                price: this.stockHistory[this.stockHistory.length - 1].close || 0,
                volume: 0,
                operate: 'nothing'
              }
            })
          this.getSuggest()
        })
    },
    async getHistory (day) {
      const res = await this.getHistoryData(this.moment(day).add(-1, 'year').format('YYYY-MM-DD'), day)
      if (res) {
        this.stockHistory = res
      }
    },
    async getHistoryData (startDate, endDate) {
      const query = {
        code: this.queryForm.stockCode,
        startDate: startDate,
        endDate: endDate
      }
      const res = await stockHistory(query)
      return res || []
    },
    async getSuggest () {
      const startDate = this.moment(this.tradeDates[this.currDayIndex]).add(-1, 'year').format('YYYY-MM-DD')
      const endDate = this.tradeDates[this.currDayIndex]
      const res = await suggestLast({}, {...this.queryForm, startDate, endDate})
      if (res) {
        this.lastSuggest = res
      }
    },
    async getSimulation () {
      const startDate = this.moment(this.tradeDates[this.currDayIndex]).add(-1, 'year').format('YYYY-MM-DD')
      const endDate = this.tradeDates[this.currDayIndex]
      const res = await simulation({}, {...this.submitForm, startDate, endDate, volume: parseInt(this.submitForm.volume || 0) * 100})
      if (res) {
        this.resultList.push(res)
      }
    }
  },
  watch: {},
  computed: {
    dataList () {
      return [...this.resultList].reverse()
    }
  }
}
</script>

<style scoped>

</style>
