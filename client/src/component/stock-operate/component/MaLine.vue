<template>
  <el-form inline>
    <el-form-item label="名称">
      <el-input v-model="params.name"></el-input>
    </el-form-item>
    <el-form-item label="天数">
      <el-input-number v-model="params.dayNum" :min="2"></el-input-number>
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
    params: {
      name: '均线',
      dayNum: 5,
      color: '#000000'
    }
  }),
  watch: {
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
      handler () {
        this.parseData()
      }
    }
  },
  methods: {
    parseData () {
      if (this.data) {
        const ret = this.dataMA(this.params.dayNum)
        this.params.name = this.params.dayNum + '日均线'
        this.$emit('change', {id: this.cKey, name: this.params.name, data: ret, color: this.params.color})
      }
    },
    dataMA (num) {
      num = num || 5
      let result = []
      for (let i = 0, len = this.data.length; i < len; i++) {
        if (i < num) {
          result.push('-')
          continue
        }
        let sum = 0
        for (let j = 0; j < num; j++) {
          sum += parseFloat(this.data[i - j])
        }
        result.push((sum / num).toFixed(2))
      }
      return result
    }
  },
  computed: {
    watchObj () {
      const {data, dateList, cKey} = this
      return {data, dateList, cKey}
    }
  }
}
</script>

<style scoped>

</style>
