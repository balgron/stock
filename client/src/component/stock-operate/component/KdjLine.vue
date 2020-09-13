<template>
  <el-form inline>
    <el-form-item label="名称">
      <el-input v-model="params.name"></el-input>
    </el-form-item>
    <el-form-item label="天数">
      <el-input-number v-model="params.dayNum" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="M1天数">
      <el-input-number v-model="params.m1" :min="2"></el-input-number>
    </el-form-item>
    <el-form-item label="M2天数">
      <el-input-number v-model="params.m2" :min="2"></el-input-number>
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
      name: 'KDJ',
      dayNum: 9,
      m1: 3,
      m2: 3,
      color1: '#000000',
      color2: '#95342b',
      color3: '#1f3bdb'
    },
    kLine: [],
    dLine: [],
    jLine: []
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
      const rsvArr = []
      const kArr = []
      const dArr = []
      const jArr = []
      for (let i = 0; i < this.params.dayNum - 1; i++) {
        rsvArr.push(50)
        kArr.push(50)
        dArr.push(50)
        jArr.push(50)
      }
      for (let i = this.params.dayNum - 1; i < this.data.length; i++) {
        let max = -1
        let min = 1e9
        for (let j = i; j >= 0; j--) {
          max = parseFloat(this.data[j]) > max ? parseFloat(this.data[j]) : max
          min = parseFloat(this.data[j]) < min ? parseFloat(this.data[j]) : min
        }
        const rsv = (parseFloat(this.data[i]) - min) / (max - min) * 100
        rsvArr.push(rsv)
        const k = this.mean(rsvArr, this.params.m1)
        kArr.push(k.toFixed(2))
        const d = this.mean(kArr, this.params.m2)
        const j = 3 * k - 2 * d
        dArr.push(d.toFixed(2))
        jArr.push(j.toFixed(2))
      }
      this.kLine = kArr
      this.dLine = dArr
      this.jLine = jArr
      const arr = [
        {id: this.cKey + '_1', name: this.params.name + '_K', data: this.kLine, color: this.params.color1, xAxisIndex: 1, yAxisIndex: 1},
        {id: this.cKey + '_2', name: this.params.name + '_D', data: this.dLine, color: this.params.color2, xAxisIndex: 1, yAxisIndex: 1},
        {id: this.cKey + '_3', name: this.params.name + '_J', data: this.jLine, color: this.params.color3, xAxisIndex: 1, yAxisIndex: 1}
      ]
      this.$emit('change', arr)
    },
    mean (arr, dayNum) {
      let sum = 0
      const index = arr.length - 1
      for (let j = dayNum - 1; j >= 0; j--) {
        if (index - j >= 0) {
          sum += parseFloat(arr[index - j])
        } else {
          sum += 50
        }

      }
      return sum / dayNum
    }
  }
}
</script>

<style scoped>

</style>
