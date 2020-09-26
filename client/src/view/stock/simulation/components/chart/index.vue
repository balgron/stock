<template>
  <div id="chart"></div>
</template>

<script>
import echarts from 'echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/tooltip'
import {formatTime} from 'echarts/lib/util/format'
export default {
  props: {
    historyData: {
      type: Array,
      default: () => []
    },
    operateData: {
      type: Array,
      default: () => []
    }
  },
  data: () => ({
    chart: null
  }),
  mounted () {
    this.$nextTick(() => {
      this.chart = echarts.init(document.getElementById('chart'))
    })
  },
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
    dates () {
      return (this.historyData || []).map(e => e.day).sort()
    },
    markPoints () {
      const tmp = {};
      (this.historyData || []).forEach(e => {
        tmp[e.day] = e.close
      })
      return (this.operateData || [])
        .filter(e => e.operate !== 'nothing')
        .map(e => ({
          name: e.date,
          value: e.operate === 'sale' ? '卖出' : '买进',
          xAxis: e.date,
          yAxis: tmp[e.date] || 0,
          itemStyle: {
            color: e.operate === 'sale' ? '#4673d2' : '#c01db3'
          }
        }))
    },
    options () {
      return {
        animation: true,
        title: {
          left: 'center',
          text: 'K线图'
        },
        color: ['#61a0a8'],
        legend: {
          top: 30,
          data: ['日K']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          backgroundColor: 'rgba(245, 245, 245, 0.8)',
          borderWidth: 1,
          borderColor: '#ccc',
          padding: 10,
          textStyle: {
            color: '#000'
          },
          position: function (pos, params, el, elRect, size) {
            let obj = {top: 10}
            obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30
            return obj
          },
          extraCssText: 'width: 170px'
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: false
            },
            brush: {
              type: ['lineX', 'clear']
            }
          }
        },
        brush: {
          xAxisIndex: 'all',
          brushLink: 'all',
          outOfBrush: {
            colorAlpha: 0.1
          }
        },
        axisPointer: {
          link: [{
            xAxisIndex: [0, 1]
          }]
        },
        xAxis: [
          {
            type: 'category',
            data: this.dates,
            boundaryGap: false,
            axisLine: {lineStyle: {color: '#777'}},
            axisLabel: {
              formatter: function (value) {
                return formatTime('MM-dd', value)
              }
            },
            min: 'dataMin',
            max: 'dataMax',
            axisPointer: {
              show: true
            }
          },
          {
            type: 'category',
            gridIndex: 1,
            data: this.dates,
            scale: true,
            boundaryGap: false,
            splitLine: {show: false},
            axisLabel: {show: false},
            axisTick: {show: false},
            axisLine: {lineStyle: {color: '#777'}},
            splitNumber: 20,
            min: 'dataMin',
            max: 'dataMax'
          }
        ],
        yAxis: [
          {
            scale: true,
            splitLine: {
              show: false
            }
          },
          {
            scale: true,
            gridIndex: 1,
            axisLabel: {show: false},
            axisLine: {show: false},
            axisTick: {show: false},
            splitLine: {show: false}
          }
        ],
        dataZoom: [
          {
            type: 'inside',
            xAxisIndex: [0, 1],
            start: 0,
            end: 100
          }
        ],
        grid: [
          {
            left: 20,
            right: 20,
            top: 110,
            height: 420
          },
          {
            left: 20,
            right: 20,
            top: 110,
            height: 420
          }
        ],
        series: [
          {
            type: 'line',
            name: '日K',
            data: (this.historyData || []).map(e => e.close),
            markPoint: {
              symbol: 'pin',
              data: this.markPoints
            }
          }
        ]
      }
    }
  }
}
</script>

<style scoped>
  #chart {
    height: 650px;
  }
</style>
