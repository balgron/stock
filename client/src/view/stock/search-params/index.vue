<template>
  <el-row>
    <el-col>
      <el-form inline>
        <el-form-item label="股票">
          <stock-select v-model="queryForm.tsCode"></stock-select>
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
        <el-form-item>
          <el-button type="primary" @click="runTask">提交任务</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col>
      <hyper-params-arr v-model="queryForm.hyperParams" :code="queryForm.strategyCode"></hyper-params-arr>
    </el-col>
    <el-col>
      <s-table :table-data="table.data">
        <el-table-column label="股票代码" prop="tsCode"></el-table-column>
        <el-table-column prop="strategyCode" label="策略代号"></el-table-column>
        <el-table-column prop="createTime" label="创建时间">
          <template slot-scope="{ row }">
            {{row.createTime ? moment(row.createTime).format('YYYY-MM-DD') : ''}}
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期"></el-table-column>
        <el-table-column prop="endDate" label="截至日期"></el-table-column>
        <el-table-column prop="initMoney" label="初始金额"></el-table-column>
        <el-table-column prop="profit" label="利润率">
          <template slot-scope="{ row }">
            <span>{{((row.profit || 0) * 100.0).toFixed(2)}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="bestParams" label="最优超参数">
          <template slot-scope="{ row }">
            <el-popover trigger="hover">
              <div style="width: 200px">
                <el-input type="textarea" autosize readonly :value="changeJson(row.bestParams)" ></el-input>
              </div>
              <span slot="reference">{{row.bestParams}}</span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="params" label="超参数">
          <template slot-scope="{ row }">
            <el-popover trigger="hover">
              <div style="width: 200px">
                <el-input type="textarea" autosize readonly :value="changeJson(row.params)" ></el-input>
              </div>
              <span slot="reference">{{row.params}}</span>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="结果">
          <template slot-scope="{ row }">
            <el-tag v-if="row.state===1" type="success">成功</el-tag>
            <el-tag v-else-if="row.state === 0">未完成</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
      </s-table>
    </el-col>
  </el-row>
</template>

<script>
import HyperParamsArr from './components/hyper-params-arr'
import {bestParamsList, findBestParams} from '@/api/stock'
export default {
  components: {HyperParamsArr},
  data: () => ({
    queryForm: {
      tsCode: '',
      endDate: '',
      startDate: '',
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    },
    table: {
      data: []
    }
  }),
  mounted () {
    this.queryForm = {
      tsCode: 'sz000001',
      endDate: this.moment().format('YYYY-MM-DD'),
      startDate: this.moment().add(-1, 'year').format('YYYY-MM-DD'),
      strategyCode: 'turtle',
      hyperParams: {},
      initMoney: 100000
    }
  },
  methods: {
    changeJson (str) {
      return JSON.stringify(JSON.parse(str || '{}'), undefined, 2)
    },
    async handleQuery () {
      const res = await bestParamsList()
      if (res) {
        this.table.data = res
      }
    },
    async runTask () {
      try {
        const res = await findBestParams({}, this.queryForm)
        if (res) {
          this.$message.success('提交成功')
        } else {
          this.$message.error('提交失败')
        }
      } catch (e) {
        this.$message.error('提交失败')
      }
    },
    handleDate (e) {
      this.queryForm = {
        ...this.queryForm,
        startDate: e[0],
        endDate: e[1]
      }
    }
  },
  watch: {},
  computed: {}
}
</script>

<style scoped>

</style>
