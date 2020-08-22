<template>
  <el-form inline>
    <el-form-item label="名称">
      <el-input v-model="params.name"></el-input>
    </el-form-item>
    <el-form-item label="天数">
      <el-input-number v-model="params.dayNum" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="最小天数">
      <el-input-number v-model="params.minDayNum" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="最大天数">
      <el-input-number v-model="params.maxDayNum" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="DEA天数">
      <el-input-number v-model="params.deaDayNum" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="颜色">
      <el-color-picker v-model="params.color1"></el-color-picker>
      <el-color-picker v-model="params.color2"></el-color-picker>
      <el-color-picker v-model="params.color3"></el-color-picker>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    data: {
      type: Array,
      default: () => []
    },
    dateList: {
      type: Array,
      default: () => []
    },
    cKey: {
      type: String,
      default: () => ''
    }
  },
  data: () => ({
    params: {
      name: 'MACD',
      dayNum: 5,
      minDayNum: 12,
      maxDayNum: 26,
      deaDayNum: 9,
      color1: '#000000',
      color2: '#95342b',
      color3: '#1f3bdb'
    },
    dif: [],
    dea: [],
    macd: []
  }),
  watch: {
    params: {
      deep: true,
      immediate: true,
      handler () {
        this.handleChange()
      }
    }
  },
  methods: {
    handleChange () {
      const week = this.mean(this.data, this.params.dayNum)
      const minMean = this.mean(week, this.params.minDayNum)
      const maxMean = this.mean(week, this.params.maxDayNum)
      const dif = []
      for (let i = 0; i < minMean.length; i++) {
        if (!maxMean[i]) {
          dif.push(undefined)
        } else {
          dif.push(minMean[i] - maxMean[i])
        }
      }
      const dea = this.mean(dif, this.params.deaDayNum)
      const macd = []
      for (let i = 0; i < dif.length; i++) {
        if (!dea[i] || !dif[i]) {
          macd.push(undefined)
        } else {
          macd.push(2 * (dif[i] - dea[i]))
        }
      }
      this.dif = dif.map(e => e ? e.toFixed(2) : undefined)
      this.dea = dea.map(e => e ? e.toFixed(2) : undefined)
      this.macd = macd.map(e => e ? e.toFixed(2) : undefined)
      const arr = [
        {id: this.cKey + '_1', name: this.params.name + 'diff', data: this.dif, color: this.params.color1, xAxisIndex: 1, yAxisIndex: 1},
        {id: this.cKey + '_2', name: this.params.name + 'dea', data: this.dea, color: this.params.color2, xAxisIndex: 1, yAxisIndex: 1},
        {id: this.cKey + '_3', name: this.params.name, data: this.macd, color: this.params.color3, type: 'bar', xAxisIndex: 1, yAxisIndex: 1}
      ]
      this.$emit('change', arr)
    },
    mean (arr, dayNum, df) {
      let array = []
      for (let i = 0; i < arr.length; i++) {
        if (i >= dayNum - 1 && !!arr[i]) {
          let sum = 0
          for (let j = dayNum - 1; j >= 0; j--) {
            sum += parseFloat(arr[i - j])
          }
          array.push(sum / dayNum)
        } else {
          array.push(df)
        }
      }
      return array
    }
  }
}
</script>

<style scoped>

</style>
