<template>
  <el-row>
    <el-row>
      <el-card>
        <el-row>
          <el-col :span="2">创建时间:</el-col>
          <el-col :span="3">{{moment(result.createTime).format('YYYY-MM-DD HH:mm')}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="2">截至时间:</el-col>
          <el-col :span="3">{{result.endDate}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="2">初始金额:</el-col>
          <el-col :span="3">{{result.initMoney}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="2">策略:</el-col>
          <el-col :span="3">{{result.strategyCode}}</el-col>
        </el-row>
        <el-row>
          <el-col :span="2">参数:</el-col>
          <el-col :span="22">
            <el-row>
              <template v-for="(v, k) in JSON.parse(result.params|| '{}')">
                <el-col :span="2" :key="k">{{k}}:</el-col>
                <el-col :span="2" :key="k+'1'">
                  <el-tag>{{v}}</el-tag>
                </el-col>
              </template>
            </el-row>
          </el-col>
        </el-row>
      </el-card>
    </el-row>
    <el-row>
      <s-table :table-data="table.data1">
        <el-table-column label="股票代号" prop="tsCode"></el-table-column>
        <el-table-column label="第一年利润" prop="one" sortable>
          <template slot-scope="{ row }">
            <span> {{ percentFormat(row.one) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="第二年利润" prop="two" sortable>
          <template slot-scope="{ row }">
            <span> {{ percentFormat(row.two) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="第三年利润" prop="three" sortable>
          <template slot-scope="{ row }">
            <span> {{ percentFormat(row.three) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="总利润" prop="total" sortable>
          <template slot-scope="{ row }">
            <span> {{ percentFormat(row.total) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{ row }">
            <el-link icon="el-icon-eye" @click="getDetail(row.tsCode)">查看详情</el-link>
          </template>
        </el-table-column>
      </s-table>
    </el-row>
    <el-row>
      <description :result-arr="table.data2" :value="show"></description>
    </el-row>
  </el-row>
</template>

<script>
import {runResultDetail, runResultList, stockStrategyHistoryById} from '@/api/stock'
import Description from './components/description'
export default {
  components: {Description},
  data: () => ({
    id: '',
    result: {},
    show: false,
    table: {
      data1: [],
      data2: []
    }
  }),
  mounted () {
    this.id = this.$route.query.id || ''
  },
  methods: {
    init () {
      this.getResultList()
      this.getResult()
    },
    async getResultList () {
      const res = await runResultList({id: this.id})
      if (res) {
        this.table.data1 = res
      }
    },
    async getResult () {
      const res = await stockStrategyHistoryById({id: this.id})
      if (res) {
        this.result = res
      }
    },
    async getDetail (tsCode) {
      const res = await runResultDetail({id: this.id, tsCode})
      if (res) {
        this.show = true
        this.table.data2 = res
      }
    },
    percentFormat (num) {
      return ((num || 0) * 100.0).toFixed(2) + '%'
    }
  },
  watch: {
    id: {
      immediate: true,
      handler () {
        if (this.id) {
          this.init()
        }
      }
    }
  },
  computed: {}
}
</script>

<style scoped>

</style>
