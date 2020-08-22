<template>
  <el-row>
    <el-col :span="24">
      <div>最高价: {{base.max.close}} ({{base.max.day}})</div>
      <div>最低价: {{base.min.close}} ({{base.min.day}})</div>
    </el-col>
    <el-col :span="24" id="chart" class="chart"></el-col>
  </el-row>
</template>

<script>
import echarts from 'echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/chart/candlestick'
import 'echarts/lib/chart/bar'
import 'echarts/lib/component/tooltip'
import { formatTime } from 'echarts/lib/util/format'
export default {
  props: {
    chartData: {
      type: Array,
      default: () => []
    },
    title: {
      type: String,
      default: 'K线图'
    }
  },
  data: () => ({
    chart: null,
    base: {
      max: {
        close: 0
      },
      min: {
        close: 0
      }
    },
    colorList: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
    options: {
      animation: true,
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
      yAxis: [
        {
          scale: true,
          splitArea: {
            show: true
          }
        },
        {
          scale: true,
          gridIndex: 1,
          splitNumber: 2,
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
        },
        {
          show: true,
          xAxisIndex: [0, 1],
          type: 'slider',
          top: '85%',
          start: 0,
          end: 100
        }
      ],
      grid: [
        {
          left: 20,
          right: 20,
          top: 110,
          height: 120
        },
        {
          left: 20,
          right: 20,
          height: 40,
          top: 260
        }
      ]
    }
  }),
  mounted () {
    this.$nextTick(() => {
      this.chart = echarts.init(document.getElementById('chart'))
    })
  },
  watch: {
    chartData () {
      if (this.chart && this.chartData && this.chartData.length > 0) {
        this.handleBase()
        this.chart.clear()
        this.handleChange()
        this.chart.setOption(this.options)
      }
    }
  },
  methods: {
    handleBase () {
      let min = {}
      let max = {}
      this.chartData.forEach(e => {
        if (!min.close || parseFloat(min.close) >= parseFloat(e.close)) {
          min = e
        }
        if (!max.close || parseFloat(max.close) <= parseFloat(e.close)) {
          max = e
        }
      })
      this.base = {
        min, max
      }
    },
    handleChange () {
      this.options = {
        ...this.options,
        title: {
          left: 'center',
          text: this.title
        },
        color: this.colorList,
        legend: {
          top: 30,
          data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30']
        },
        xAxis: this.xAxis(),
        graphic: this.graphic(),
        series: this.series()
      }
    },
    xAxis () {
      const dates = this.chartData.map(e => e.day)
      return [
        {
          type: 'category',
          data: dates,
          boundaryGap: false,
          axisLine: { lineStyle: { color: '#777' } },
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
          data: dates,
          scale: true,
          boundaryGap: false,
          splitLine: {show: false},
          axisLabel: {show: false},
          axisTick: {show: false},
          axisLine: { lineStyle: { color: '#777' } },
          splitNumber: 20,
          min: 'dataMin',
          max: 'dataMax'
        }
      ]
    },
    series () {
      const volumes = this.chartData.map(e => parseInt(e.volume))
      const chartData = this.chartData
      return [
        {
          name: '成交量',
          type: 'bar',
          xAxisIndex: 1,
          yAxisIndex: 1,
          itemStyle: {
            normal: {
              color: function (params) {
                const data = chartData[params.dataIndex]
                return data.open > data.close ? '#14b143' : '#ef232a'
              }
            }
          },
          emphasis: {
            itemStyle: {
              color: '#4243d8'
            }
          },
          data: volumes
        },
        {
          type: 'candlestick',
          name: '日K',
          data: this.transformData(),
          itemStyle: {
            color: '#ef232a',
            color0: '#14b143',
            borderColor: '#ef232a',
            borderColor0: '#14b143'
          },
          emphasis: {
            itemStyle: {
              color: 'black',
              color0: '#444',
              borderColor: 'black',
              borderColor0: '#444'
            }
          },
          tooltip: {
            formatter: function (param) {
              param = param[0]
              return [
                '日期: ' + param.name + '<hr size=1 style="margin: 3px 0">',
                '开盘价: ' + param.data[0] + '<br/>',
                '收盘价: ' + param.data[1] + '<br/>',
                '最低价: ' + param.data[2] + '<br/>',
                '最高价: ' + param.data[3] + '<br/>'
              ].join('')
            }
          }
        },
        {
          name: 'MA5',
          type: 'line',
          data: this.dataMA(5),
          smooth: true,
          showSymbol: false,
          lineStyle: {
            width: 1
          }
        },
        {
          name: 'MA10',
          type: 'line',
          data: this.dataMA(10),
          smooth: true,
          showSymbol: false,
          lineStyle: {
            width: 1
          }
        },
        {
          name: 'MA20',
          type: 'line',
          data: this.dataMA(20),
          smooth: true,
          showSymbol: false,
          lineStyle: {
            width: 1
          }
        }
      ]
    },
    graphic () {
      return [{
        type: 'group',
        left: 'center',
        top: 70,
        width: 300,
        bounding: 'raw',
        children: [{
          id: 'MA5',
          type: 'text',
          style: {fill: this.colorList[1]},
          left: 0
        }, {
          id: 'MA10',
          type: 'text',
          style: {fill: this.colorList[2]},
          left: 'center'
        }, {
          id: 'MA20',
          type: 'text',
          style: {fill: this.colorList[3]},
          right: 0
        }]
      }]
    },
    transformData () {
      const data = []
      this.chartData.forEach(e => {
        data.push([e.open, e.close, e.low, e.high])
      })
      return data
    },
    dataMA (num) {
      let result = []
      for (let i = 0, len = this.chartData.length; i < len; i++) {
        if (i < num) {
          result.push('-')
          continue
        }
        let sum = 0
        for (let j = 0; j < num; j++) {
          sum += parseFloat(this.chartData[i - j].close)
        }
        result.push((sum / num).toFixed(2))
      }
      return result
    }
  }
}
</script>

<style scoped>
 .chart {
   height: 360px;
 }
</style>
