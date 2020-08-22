<template>
  <el-select autocomplete filterable :filter-method="filterMethod" v-model="selectVal">
    <el-option v-for="strategy in strategyArr" :label="strategy.name" :value="strategy.code" :key="strategy.code"></el-option>
  </el-select>
</template>

<script>
import {backTestStrategy} from '@/api/stock'

export default {
  props: {
    value: {
      type: String,
      require: true
    }
  },
  data: () => ({
    selectVal: '',
    search: '',
    strategyList: []
  }),
  watch: {
    value: {
      immediate: true,
      handler () {
        if (this.value !== this.selectVal) {
          this.selectVal = this.value
        }
      }
    },
    selectVal (val) {
      this.$emit('input', val)
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    async init () {
      const res = await backTestStrategy()
      if (res) {
        this.strategyList = res
      }
    },
    filterMethod (key) {
      this.search = key
    }
  },
  computed: {
    strategyArr () {
      const key = this.search
      return this.strategyList.filter(e => key == null || e.code.indexOf(key) > -1 || e.name.indexOf(key) > -1)
    }
  }
}
</script>

<style scoped>

</style>
