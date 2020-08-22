<template>
  <el-row>
    <el-col>
      <el-form inline>
        <el-form-item label="截至时间">
          <el-date-picker
            v-model="queryForm.endDate"
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
          <el-button type="primary" @click="runStrategy">后台运行</el-button>
          <el-tag>寻找最近三年该策略的收益情况</el-tag>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col>
      <hyper-params v-model="queryForm.hyperParams" :code="queryForm.strategyCode"></hyper-params>
    </el-col>
    <el-col>
      <s-table :table-data="table.data">
        <el-table-column prop="strategyCode" label="策略代号"></el-table-column>
        <el-table-column prop="createTime" label="创建时间">
          <template slot-scope="{ row }">
            {{row.createTime ? moment(row.createTime).format('YYYY-MM-DD') : ''}}
          </template>
        </el-table-column>
        <el-table-column prop="initMoney" label="初始金额"></el-table-column>
        <el-table-column prop="params" label="超参数">
          <template slot-scope="{ row }">
            <el-popover trigger="hover">
              <el-input type="textarea" autosize readonly :value="changeJson(row.params)"></el-input>
              <span slot="reference">{{row.params}}</span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="截至日期"></el-table-column>
        <el-table-column label="结果">
          <template slot-scope="{ row }">
            <el-tag v-if="row.state===1" type="success">成功</el-tag>
            <el-tag v-else-if="row.state === 0">未完成</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{ row }">
            <el-link v-if="row.state===1" icon="el-icon-view" @click="runResult(row.uuid)">查看</el-link>
            <el-link v-else-if="row.state===-1" type="danger" @click="reRunStrategy(row.uuid)">重跑</el-link>
          </template>
        </el-table-column>
      </s-table>
    </el-col>
  </el-row>
</template>

<script>
import {reRunSearchBest, runSearchBest, stockStrategyHistory} from '@/api/stock'

export default {
  data: () => ({
    queryForm: {
      endDate: '',
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    },
    table: {
      data: []
    },
    processResult: undefined,
    timeId: null
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
    this.init()
    this.timeId = setInterval(() => this.init(), 5000)
  },
  beforeDestroy () {
    clearInterval(this.timeId)
  },
  methods: {
    changeJson (str) {
      return JSON.stringify(JSON.parse(str || '{}'), undefined, 2)
    },
    async init () {
      const res = await stockStrategyHistory()
      if (res) {
        this.table.data = res
      }
    },
    async runStrategy () {
      try {
        const res = await runSearchBest({}, this.queryForm)
        if (res) {
          this.$message.success('提交成功')
        } else {
          this.$message.error('提交失败')
        }
      } catch (e) {
        this.$message.error('提交失败')
      }
    },
    async reRunStrategy (id) {
      try {
        const res = await reRunSearchBest({id})
        if (res) {
          this.$message.success('提交成功')
        } else {
          this.$message.error('提交失败')
        }
      } catch (e) {
        this.$message.error('提交失败')
      }
    },
    runResult (id) {
      this.$router.push({
        path: '/stock-quantify/search-best/detail',
        query: {
          id
        }
      }).catch(() => {
      })
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

<style>
  .el-table td .cell {
    white-space: nowrap;
  }
</style>
