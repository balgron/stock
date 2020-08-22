<template>
  <el-dialog :visible.sync="value" width="80%" @open="open" @close="close">
    <el-row>
      <el-col v-for="(e, i) in resultArr" :span="6" :key="i">
        <el-card>
          <el-row>
            <el-col :span="10">股票代号:</el-col>
            <el-col :span="14">{{e.tsCode}}</el-col>
          </el-row>
          <el-row>
            <el-col :span="10">时间:</el-col>
            <el-col :span="14">{{e.startDate}} ~ {{e.endDate}}</el-col>
          </el-row>
          <el-row v-for="(v, k) in propsText" :key="k">
            <el-col :span="10">{{v}}:</el-col>
            <el-col :span="14">{{propsMethod[k](e, k)}}</el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
export default {
  props: {
    resultArr: {
      type: Array,
      default: () => []
    },
    value: {
      type: Boolean,
      default: false
    }
  },
  data: () => ({
    propsText: {
      profit: '利润率',
      tradeNum: '总交易次数',
      meanProfit: '平均每笔回报利润率',
      maxProfit: '最大获利',
      maxLoss: '最大亏损',
      profitLossRate: '盈亏比',
      profitNum: '盈利笔数',
      lossNum: '亏损笔数',
      profitRate: '盈利比例',
      maxLossNum: '最大连续亏损笔数',
      maxLossMoney: '最大连续亏损金额'
    },
    propsMethod: {}
  }),
  mounted () {
    this.init()
  },
  methods: {
    open () {
      this.$emit('change', true)
    },
    close () {
      this.$emit('change', false)
    },
    init () {
      this.propsMethod = {
        profit: (o, e) => this.toPercent(o, e),
        tradeNum: (o, e) => this.toInt(o, e),
        meanProfit: (o, e) => this.toPercent(o, e),
        maxProfit: (o, e) => this.toPercent(o, e),
        maxLoss: (o, e) => this.toPercent(o, e),
        profitLossRate: (o, e) => this.toPercent(o, e),
        profitNum: (o, e) => this.toInt(o, e),
        lossNum: (o, e) => this.toInt(o, e),
        profitRate: (o, e) => this.toPercent(o, e),
        maxLossNum: (o, e) => this.toInt(o, e),
        maxLossMoney: (o, e) => this.toFloat(o, e)
      }
    },
    toPercent (obj, key) {
      const num = obj[key]
      return ((num || 0) * 100).toFixed(2) + '%'
    },
    toInt (obj, key) {
      const num = obj[key]
      return (num || 0).toFixed(0)
    },
    toFloat (obj, key) {
      const num = obj[key]
      return (num || 0).toFixed(2)
    }
  },
  watch: {},
  computed: {}
}
</script>

<style scoped>

</style>
