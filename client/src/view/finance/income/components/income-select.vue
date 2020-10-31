<template>
  <el-select :clearable="clearable" v-model="selectVal">
    <el-option v-for="item in list" :value="item.value" :label="item.text" :key="item.value"></el-option>
  </el-select>
</template>

<script>

import {incomeType} from '@/api/finance'

export default {
  name: 'income-select',
  props: {
    value: {
      type: [Number, String]
    },
    clearable: {
      type: Boolean,
      default: true
    }
  },
  data: () => ({
    selectVal: [],
    list: []
  }),
  watch: {
    value: {
      immediate: true,
      handler (val) {
        this.selectVal = val
      }
    },
    selectVal () {
      this.$emit('input', this.selectVal)
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    async init () {
      const res = await incomeType()
      if (res) {
        this.list = res
      }
    },
    getText (num) {
      return this.list.find(e => e.value === num) || {}
    }
  }
}
</script>

<style scoped>

</style>
