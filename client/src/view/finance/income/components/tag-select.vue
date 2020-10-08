<template>
    <el-select clearable multiple v-model="selectVal">
      <el-option v-for="item in list" :value="item.value" :label="item.text" :key="item.value"></el-option>
    </el-select>
</template>

<script>

import {consumeType} from '@/api/finance'

export default {
  name: 'tag-select',
  props: {
    value: {
      type: Array,
      default: () => []
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
    selectVal: {
      deep: true,
      handler () {
        this.$emit('input', this.selectVal)
      }
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    async init () {
      const res = await consumeType()
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
