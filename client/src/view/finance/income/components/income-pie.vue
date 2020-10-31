<template>
  <div ref="chart" style="height: 170px"></div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: 'income-pie',
  props: {
    tableData: {
      type: Array,
      default: () => []
    }
  },
  data: () => ({
    chart: null
  }),
  mounted () {
    this.$nextTick(() => {
      this.chart = echarts.init(this.$refs.chart)
    })
  },
  methods: {},
  watch: {
    options () {
      if (this.chart) {
        this.chart.clear()
        this.chart.setOption(this.options)
      }
    },
    chart () {
      if (this.chart) {
        this.chart.clear()
        this.chart.setOption(this.options)
      }
    }
  },
  computed: {
    options () {
      return {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          right: 10,
          top: 20,
          bottom: 20,
          data: ['支出', '收入']
        },
        series: [
          {
            name: '金',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: this.computedData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    },
    computedData () {
      let come = 0
      let pay = 0
      this.tableData.filter(e => !e.type).forEach(e => {
        come += e.money || 0
      })
      this.tableData.filter(e => e.type).forEach(e => {
        pay += e.money || 0
      })
      return [
        {
          name: '支出',
          value: pay.toFixed(2)
        },
        {
          name: '收入',
          value: come.toFixed(2)
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
