<template>
  <el-select autocomplete filterable :filter-method="filterMethod" v-model="selectVal">
    <el-option v-for="stock in stockArr" :label="`${stock.name}(${stock.tsCode})`" :value="stock.tsCode" :key="stock.tsCode"></el-option>
  </el-select>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      require: true
    }
  },
  data: () => ({
    selectVal: '',
    search: ''
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
  methods: {
    filterMethod (key) {
      this.search = key
    }
  },
  computed: {
    stocks () {
      return this.$store.getters.stocks
    },
    stockArr () {
      const key = this.search
      return this.stocks.filter(e => key == null || e.tsCode.indexOf(key) > -1 || e.name.indexOf(key) > -1)
    }
  }
}
</script>

<style scoped>

</style>
