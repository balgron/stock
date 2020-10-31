<template>
  <el-row style="margin-top: 10px">
    <el-col :span="6">
      <el-card header="收入/支出饼图">
        <income-pie :table-data="data"></income-pie>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card header="收入/支出线图">
        <income-line :table-data="data"></income-line>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import IncomePie from '@/view/finance/income/components/income-pie'
import IncomeLine from '@/view/finance/income/components/income-line'
export default {
  name: 'income-chart',
  components: {IncomeLine, IncomePie},
  props: {
    data: {
      type: Array,
      default: () => []
    }
  }
}
</script>

<style scoped>

</style>
