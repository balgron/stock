<template>
  <div>
    <el-form inline>
      <template v-for="param in params">
        <el-form-item :label="param.description" :key="param.name">
          <el-input-number v-model="input[param.name]" v-if="param.type==='number'"></el-input-number>
          <el-input v-model="input[param.name]" v-else></el-input>
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
import {backTestStrategyParams} from '@/api/stock'

export default {
  props: {
    value: {
      type: Object,
      default: () => ({})
    },
    code: {
      type: String
    }
  },
  data: () => ({
    input: {},
    paramList: []
  }),
  watch: {
    code: {
      immediate: true,
      handler () {
        this.handleChange()
      }
    },
    input: {
      deep: true,
      handler (val) {
        this.$emit('change', val)
        this.$emit('input', val)
      }
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    async init () {
      const res = await backTestStrategyParams()
      if (res) {
        this.paramList = res
        this.handleChange()
      }
    },
    handleChange () {
      const ret = {}
      this.params.forEach(e => {
        ret[e.name] = e.default
      })
      this.input = ret
      this.$emit('change', ret)
      this.$emit('input', ret)
    }
  },
  computed: {
    params () {
      if (!this.code) {
        return []
      }
      const res = (this.paramList || []).find(e => e.code === this.code)
      return (res || {}).params || []
    }
  }
}
</script>

<style scoped>

</style>
