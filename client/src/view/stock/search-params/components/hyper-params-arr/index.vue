<template>
  <div>
    <el-form inline>
      <template v-for="param in params">
        <el-col :key="param.name">
          <el-form-item :label="param.description">
            <template v-if="param.type==='number'">
              <el-input-number v-model="input[param.name][0]"></el-input-number>
              <span style="margin: 0 10px">-</span>
              <el-input-number v-model="input[param.name][1]"></el-input-number>
            </template>
            <template v-else>
              <el-select allow-create multiple v-model="input[param.name]"></el-select>
            </template>

          </el-form-item>
        </el-col>
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
        if (e.type === 'number') {
          ret[e.name] = [e.default || 0, e.default || 0, 1]
        } else {
          ret[e.name] = [e.default || '']
        }
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
