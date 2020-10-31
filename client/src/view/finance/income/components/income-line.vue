<template>
  <div ref="chart" style="height: 170px"></div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: 'income-line',
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
        color: ['#ff7875', '#b7eb8f'],
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['支出', '收入']
        },
        xAxis: {
          type: 'category',
          data: this.computedDateRange
        },
        yAxis: {
          type: 'value',
          splitLine: {
            show: false
          }
        },
        series: [
          {
            name: '支出',
            type: 'line',
            stack: '金额',
            data: this.computedData[1]
          },
          {
            name: '收入',
            type: 'line',
            stack: '金额',
            data: this.computedData[0]
          }
        ]
      }
    },
    computedDateRange () {
      return Array.from(new Set(this.tableData.map(e => e.date))).sort()
    },
    computedData () {
      let come = {}
      let pay = {}
      this.tableData.filter(e => !e.type).forEach(e => {
        if (!come[e.date]) {
          come[e.date] = 0
        }
        come[e.date] += e.money || 0
      })
      this.tableData.filter(e => e.type).forEach(e => {
        if (!pay[e.date]) {
          pay[e.date] = 0
        }
        pay[e.date] += e.money || 0
      })
      const comeArr = []
      const payArr = []
      this.computedDateRange.forEach(e => {
        comeArr.push((come[e] || 0).toFixed(2))
        payArr.push((pay[e] || 0).toFixed(2))
      })
      return [
        comeArr, payArr
      ]
    }
  }
}
</script>

<style scoped>

</style>
