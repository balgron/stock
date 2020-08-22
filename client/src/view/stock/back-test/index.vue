<template>
  <el-row>
    <el-col>
      <el-form inline>
        <el-form-item label="股票">
          <stock-select v-model="queryForm.stockCode"></stock-select>
        </el-form-item>
        <el-form-item label="时间">
          <el-date-picker
            :value="[queryForm.startDate, queryForm.endDate]"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @input="handleDate"
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
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col>
      <hyper-params v-model="queryForm.hyperParams" :code="queryForm.strategyCode"></hyper-params>
    </el-col>
    <template v-if="processResult">
      <el-col>
        <description :process-result="processResult"></description>
      </el-col>
      <el-col>
        <chart :process-result="processResult"></chart>
      </el-col>
      <el-col>
        <trade :process-result="processResult"></trade>
      </el-col>
    </template>
  </el-row>
</template>

<script>
import Description from './components/description'
import Trade from './components/trade'
import Chart from './components/chart'
import {backTest} from '@/api/stock'

export default {
  components: {
    Description, Trade, Chart
  },
  data: () => ({
    queryForm: {
      stockCode: '',
      endDate: '',
      startDate: '',
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    },
    processResult: undefined
  }),
  mounted () {
    this.queryForm = {
      stockCode: 'sz000001',
      endDate: this.moment().format('YYYY-MM-DD'),
      startDate: this.moment().add(-1, 'year').format('YYYY-MM-DD'),
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    }
  },
  methods: {
    async handleQuery () {
      const res = await backTest({}, this.queryForm)
      if (res) {
        this.processResult = res
      }
    },
    handleDate (e) {
      this.queryForm = {
        ...this.queryForm,
        startDate: e[0],
        endDate: e[1]
      }
    }
  }
}
</script>

<style scoped>

</style>
