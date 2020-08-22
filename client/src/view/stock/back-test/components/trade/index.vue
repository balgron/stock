<template>
  <el-card>
    <el-row>
      <el-col>
        <s-table :table-data="tableData" height="500" show-all>
          <el-table-column label="时间" prop="date"></el-table-column>
          <el-table-column label="持有金额" prop="holdMoney" align="right">
            <template slot-scope="{ row }">
              <span>{{(row.holdMoney || 0 ).toFixed(2)}}</span>
            </template>
          </el-table-column>
          <el-table-column label="成交量" prop="volume" align="right"></el-table-column>
          <el-table-column label="每股价格" prop="money" align="right"></el-table-column>
          <el-table-column label="持有每股价格" prop="originMoney" align="right">
            <template slot-scope="{ row }">
              <span>{{(row.originMoney || 0 ).toFixed(2)}}</span>
            </template>
          </el-table-column>
          <el-table-column label="税费" align="right">
            <template slot-scope="{ row }">
              <span>{{(row.tax || 0 ).toFixed(2)}}</span>
            </template>
          </el-table-column>
          <el-table-column label="利润" align="right">
            <template slot-scope="{ row }">
              <span v-if="!row.sale">-</span>
              <span v-else>{{getProfit(row)}}</span>
            </template>
          </el-table-column>
          <el-table-column label="是否盈利">
            <template slot-scope="{ row }">
              <span v-if="!row.sale">-</span>
              <el-tag :type="isProfit(row) ? 'success' : 'danger'" v-else>{{isProfit(row)? '是': '否'}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="left">
            <template slot-scope="{ row }">
              <span>{{!!row.sale ? '卖出': '买进'}}</span>
            </template>
          </el-table-column>
        </s-table>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  props: {
    processResult: {
      type: Object,
      default: () => ({})
    }
  },
  data: () => ({

  }),
  methods: {
    getProfit (row) {
      return ((row.volume * (row.money - row.originMoney) - row.tax) * 100 / (row.volume * row.originMoney)).toFixed(2) + '%'
    },
    isProfit (row) {
      return row.volume * (row.money - row.originMoney) - row.tax > 0
    }
  },
  computed: {
    tableData () {
      return this.processResult.infos || []
    }
  }
}
</script>

<style scoped>

</style>
