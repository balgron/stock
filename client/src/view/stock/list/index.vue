<template>
  <el-row>
    <el-col>
      <el-form inline label-width="80">
        <el-form-item label="股票代号">
          <el-input v-model="queryForm.code"></el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col>
      <s-table :table-data="tableData">
        <el-table-column label="代号" prop="tsCode"></el-table-column>
        <el-table-column label="代号2" prop="symbol"></el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{row}">
            <el-button type="text" @click="redirect(row.tsCode)">跳转</el-button>
          </template>
        </el-table-column>
      </s-table>
    </el-col>
  </el-row>
</template>

<script>
import {stockList} from '@/api/stock'

export default {
  data: () => ({
    queryForm: {
      code: ''
    },
    tableData: []
  }),
  mounted () {
    this.handleQuery()
  },
  methods: {
    handleQuery () {
      stockList(this.queryForm)
        .then(res => {
          this.tableData = res || []
        })
    },
    redirect (code) {
      this.$router.push({
        path: '/stock/history',
        query: {
          code,
          endDate: this.moment().format('YYYY-MM-DD'),
          startDate: this.moment().add(-3, 'month').format('YYYY-MM-DD')
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
