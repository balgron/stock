<template>
  <div>
    <el-table :data="filterData" v-bind="$attrs">
      <slot></slot>
    </el-table>
    <el-pagination
      small
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="pageInfo.page"
      :page-sizes="[10, 20, 50, 100, 200]"
      :page-size="pageInfo.size"
      layout="sizes, prev, pager, next, jumper, total"
      :total="pageInfo.total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  props: {
    tableData: {
      type: Array,
      default: () => []
    },
    showAll: {
      type: Boolean,
      default: false
    }
  },
  data: () => ({
    pageInfo: {
      size: 10,
      total: 0,
      page: 1
    }
  }),
  watch: {
    tableData: {
      immediate: true,
      handler (val) {
        this.pageInfo = {
          size: this.pageInfo.size || 10,
          total: val.length,
          page: this.pageInfo.page || 1
        }
      }
    },
    showAll: {
      immediate: true,
      handler () {
        this.handleSizeChange(this.tableData.length)
      }
    }
  },
  methods: {
    handleSizeChange (e) {
      this.$set(this.pageInfo, 'size', e)
    },
    handleCurrentChange (e) {
      this.$set(this.pageInfo, 'page', e)
    }
  },
  computed: {
    filterData () {
      let start = (this.pageInfo.page - 1) * this.pageInfo.size
      let end = this.pageInfo.page * this.pageInfo.size
      return this.tableData.slice(start, end)
    }
  }
}
</script>

<style>
  .el-pagination {
    float: right;
  }
</style>
