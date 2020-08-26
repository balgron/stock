<template>
  <el-card>
    <el-row>
      <el-col :span="6" v-for="(v, k) in propsText" :key="k">
        <div class="label-box">
          <span class="label">{{v}}</span>
          <span class="text">{{propsMethod[k] ? propsMethod[k](k) : ''}}</span>
        </div>
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
    propsText: {
      profit: '利润率',
      normalProfit: '普通利润率',
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
    init () {
      this.propsMethod = {
        profit: e => this.toPercent(e),
        normalProfit: e => this.toPercent(e),
        tradeNum: e => this.toInt(e),
        meanProfit: e => this.toPercent(e),
        maxProfit: e => this.toPercent(e),
        maxLoss: e => this.toPercent(e),
        profitLossRate: e => this.toPercent(e),
        profitNum: e => this.toInt(e),
        lossNum: e => this.toInt(e),
        profitRate: e => this.toPercent(e),
        maxLossNum: e => this.toInt(e),
        maxLossMoney: e => this.toFloat(e)
      }
    },
    toPercent (key) {
      const num = this.processResult[key]
      return ((num || 0) * 100).toFixed(2) + '%'
    },
    toInt (key) {
      const num = this.processResult[key]
      return (num || 0).toFixed(0)
    },
    toFloat (key) {
      const num = this.processResult[key]
      return (num || 0).toFixed(2)
    }
  }
}
</script>

<style scoped>
  .label-box {
    display: flex;
  }

  .label {
    color: rgba(0, 0, 0, 0.5);
    flex: 4;
  }

  .label:after {
    content: ': ';
    margin-right: 12px;
  }

  .text {
    flex: 3;
  }
</style>
