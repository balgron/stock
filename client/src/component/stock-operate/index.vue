<template>
  <el-row>
    <el-col :span="24">
      <el-form inline>
        <el-form-item label="显示类型" label-width="100">
          <el-select v-model="queryForm.showType">
            <el-option label="开盘价" value="open"></el-option>
            <el-option label="收盘价" value="close"></el-option>
            <el-option label="最高价" value="high"></el-option>
            <el-option label="最低价" value="low"></el-option>
            <el-option label="成交量" value="volume"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addComponent">添加线</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" v-for="(cp,i) in componentList" :key="i">
      <el-row>
        <el-col :span="2">
          <el-select v-model="componentList[i]">
            <el-option label="直线" value="SimpleLine"></el-option>
            <el-option label="均线" value="MaLine"></el-option>
            <el-option label="MACD" value="MacdLine"></el-option>
            <el-option label="KDJ" value="KdjLine"></el-option>
          </el-select>
        </el-col>
        <el-col :span="1">
          <el-button type="danger" @click="removeComponent(i)">删除</el-button>
        </el-col>
        <el-col :span="21">
          <component :is="cp" :data="cData" :date-list="dates" :c-key="i + ''" @change="handleChange"></component>
        </el-col>
      </el-row>
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
import {formatTime} from 'echarts/lib/util/format'
import SimpleLine from '@/component/stock-operate/component/SimpleLine'
import MaLine from '@/component/stock-operate/component/MaLine'
import MacdLine from '@/component/stock-operate/component/MacdLine'
import KdjLine from '@/component/stock-operate/component/KdjLine'

export default {
  components: {SimpleLine, MaLine, MacdLine, KdjLine},
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
    queryForm: {
      showType: 'close'
    },
    componentList: [],
    operateMap: {},
    chart: null,
    colorList: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3']
  }),
  mounted () {
    this.$nextTick(() => {
      this.chart = echarts.init(document.getElementById('chart'))
    })
  },
  watch: {
    options () {
      this.chart.clear()
      this.chart.setOption(this.options)
    }
  },
  methods: {
    handleChange (data) {
      if (data.constructor === Array) {
        data.forEach(e => {
          this.$set(this.operateMap, e.id, e)
        })
      } else {
        this.$set(this.operateMap, data.id, data)
      }
    },
    addComponent () {
      this.componentList.push('SimpleLine')
    },
    removeComponent (index) {
      this.componentList.splice(index, 1)
      this.$delete(this.operateMap, index)
    }
  },
  computed: {
    operateList () {
      return Object.values(this.operateMap)
    },
    cData () {
      return (this.chartData || []).map(e => e[this.queryForm.showType])
    },
    dates () {
      return (this.chartData || []).map(e => e.day)
    },
    options () {
      return {
        animation: true,
        title: {
          left: 'center',
          text: this.title
        },
        color: this.colorList,
        legend: {
          top: 30,
          data: ['日K', ...this.operateList.map(e => e.name)]
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
              yAxisIndex: true
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
            axisLine: {lineStyle: {color: '#777'}},
            axisLabel: {
              formatter: function (value) {
                return formatTime('MM-dd', value)
              }
            },
            scale: true,
            boundaryGap: false,
            axisPointer: {
              show: true
            },
            min: 'dataMin',
            max: 'dataMax'
          }
        ],
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
            splitArea: {
              show: true
            }
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
            top: '10%',
            start: 0,
            end: 100
          }
        ],
        grid: [
          {
            left: 20,
            right: 20,
            top: 130,
            height: 360
          },
          {
            left: 20,
            right: 20,
            height: 120,
            top: 550
          }
        ],
        graphic: [{
          type: 'group',
          left: 'center',
          top: 70,
          width: 300,
          bounding: 'raw',
          children: [
            ...this.operateList.map(e => ({
              id: e.name,
              type: 'text',
              style: {fill: e.color},
              left: 0
            }))
          ]
        }],
        series: [
          {
            type: 'line',
            name: '日K',
            data: this.cData,
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
            }
          },
          ...this.operateList.map(e => ({
            type: e.type || 'line',
            symbol: 'none',
            xAxisIndex: e.xAxisIndex || 0,
            yAxisIndex: e.yAxisIndex || 0,
            name: e.name,
            data: e.data,
            lineStyle: {
              width: 1,
              color: e.color,
              type: 'dotted'
            }
          }))
        ]
      }
    }
  }
}
</script>

<style scoped>
  .chart {
    height: 700px;
  }
</style>
