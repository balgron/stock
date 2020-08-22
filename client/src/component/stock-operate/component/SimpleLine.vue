<template>
  <el-form inline>
    <el-form-item label="名称">
      <el-input v-model="name"></el-input>
    </el-form-item>
    <el-form-item label="时间">
      <el-date-picker v-model="date" type="daterange" value-format="yyyy-MM-dd" :picker-options="pickerOptions"></el-date-picker>
    </el-form-item>
    <el-form-item label="斜率">
      <el-input-number v-model="params.k" :step="0.01"></el-input-number>
    </el-form-item>
    <el-form-item label="截距">
      <el-input-number v-model="params.b" :step="0.01"></el-input-number>
    </el-form-item>
    <el-form-item label="长度">
      <el-input-number v-model="len" :step="1"></el-input-number>
    </el-form-item>
    <el-form-item label="颜色">
      <el-color-picker v-model="params.color"></el-color-picker>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'SimpleLine',
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
    date: [],
    name: '直线',
    len: 10,
    params: {
      color: '#000000',
      start: 0,
      end: 0,
      k: 0,
      b: 0
    }
  }),
  watch: {
    date (val) {
      if (!val) {
        return
      }
      this.parseData()
    },
    watchObj: {
      immediate: true,
      deep: true,
      handler (val) {
        if (Object.values(val).filter(e => !!e).length !== Object.values(val).length) {
          return
        }
        this.parseData()
      }
    },
    params: {
      deep: true,
      handler (val) {
        if (val.k === 0 && val.b === 0) {
          return
        }
        const res = []
        this.dateList.forEach((e, i) => {
          const r = val.k * i + val.b
          if (r < 0 || i + this.len < this.params.start || i - this.len > this.params.end) {
            res.push('-')
          } else {
            res.push(r.toFixed(2))
          }
        })
        this.$emit('change', {id: this.cKey, name: this.name, data: res, color: this.params.color})
      }
    }
  },
  methods: {
    parseData () {
      const arr = this.data || []
      const i = this.findIndex(this.date[0])
      const j = this.findIndex(this.date[1])
      if (i === -1 || j === -1) {
        return
      }
      const yi = parseFloat(arr[i])
      const yj = parseFloat(arr[j])
      this.params = {
        start: i,
        end: j,
        k: i === j ? 0 : (yi - yj) / (i - j),
        b: i === j ? yi : yi - ((yi - yj) / (i - j)) * i
      }
    },
    findIndex (date) {
      if (!date) {
        return -1
      }
      const arr = (this.dateList || []).sort()
      let index = -1
      arr.forEach(e => {
        if (e > date) {
          return
        }
        index++
      })
      return index
    },
    disabledDate (time) {
      const arr = (this.dateList || []).sort()
      if (arr.length === 0) {
        return false
      }
      const start = this.moment(arr[0]).startOf('day')
      const end = this.moment(arr[arr.length - 1]).startOf('day')
      const base = this.moment(time).startOf('day')
      return base.isBefore(start) && base.isAfter(end)
    }
  },
  computed: {
    pickerOptions () {
      return {
        disabledDate: this.disabledDate
      }
    },
    watchObj () {
      const {data, dateList, cKey} = this
      return {data, dateList, cKey}
    }
  }
}
</script>

<style scoped>

</style>
