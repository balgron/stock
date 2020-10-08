<template>
  <el-row>
    <el-col>
      <el-card>
        <el-form inline>
          <el-form-item label="日期">
            <el-date-picker
              type="daterange"
              value-format="yyyy-MM-dd"
              v-model="dateArr"
              @change="handleDate"
              style="width: 220px"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="类型">
            <income-select v-model="queryForm.type" ref="income"></income-select>
          </el-form-item>
          <el-form-item label="标签">
            <tag-select v-model="queryForm.tags" ref="tag"></tag-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery" type="primary">查询</el-button>
            <el-button @click="handleAdd" type="primary">添加</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <el-col>
      <s-table :table-data="table.data" border show-summary :summary-method="summary">
        <el-table-column label="时间">
          <template slot-scope="{ row }">
            <span>{{row.time ? moment(row.time).format('YYYY-MM-DD HH:mm') : ''}}</span>
          </template>
        </el-table-column>
        <el-table-column label="类型">
          <template slot-scope="{ row }">
            <span>{{$refs.income.getText(row.type).text}}</span>
          </template>
        </el-table-column>
        <el-table-column label="标签">
          <template slot-scope="{ row }">
            <el-tag v-for="item in row.tags" :key="item" effect="plain" :style="color($refs.tag.getText(item))">
              {{$refs.tag.getText(item).text}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="金额" prop="money"></el-table-column>
        <el-table-column label="说明" property="description" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="{ row }">
            <el-link icon="el-icon-edit" @click="handleEdit(row)" type="primary">编辑</el-link>
            <el-popconfirm title="确定删除吗？" @onConfirm="handleDelete(row.id)">
              <el-link icon="el-icon-delete" type="danger" slot="reference">删除</el-link>
            </el-popconfirm>
          </template>
        </el-table-column>
      </s-table>
    </el-col>
    <el-col>
      <income-chart></income-chart>
    </el-col>
    <el-dialog :visible.sync="dialog.show" :title="dialog.title" show-close width="20%" :close-on-click-modal="false">
      <el-form label-width="70px">
        <el-form-item label="日期">
          <el-date-picker
            v-model="submitForm.time"
            type="datetime"
            placeholder="选择日期时间"
            value-format="timestamp"
            align="right"
            @change="handleChange"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="类型">
          <income-select v-model="submitForm.type"></income-select>
        </el-form-item>
        <el-form-item label="标签">
          <tag-select v-model="submitForm.tags"></tag-select>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number v-model="submitForm.money" :step="0.02"></el-input-number>
        </el-form-item>
        <el-form-item label="附加说明">
          <el-input v-model="submitForm.description" type="textarea" :row="7"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-row>
</template>

<script>
import IncomeSelect from './components/income-select'
import TagSelect from './components/tag-select'
import {deleteIncome, editIncome, getIncomeList, saveIncome} from '@/api/finance'
import IncomeChart from '@/view/finance/income/components/income-chart'

export default {
  components: {IncomeChart, TagSelect, IncomeSelect},
  data () {
    return {
      queryForm: {
        endDate: this.moment().format('YYYY-MM-DD'),
        startDate: this.moment().add(-7, 'day').format('YYYY-MM-DD'),
        type: '',
        tags: []
      },
      submitForm: {
        date: '',
        time: '',
        money: 0.00,
        type: 0,
        tags: [],
        description: ''
      },
      dialog: {
        show: false,
        title: ''
      },
      pickerOptions: {
        shortcuts: [
          {
            text: '现在',
            onClick (picker) {
              picker.$emit('pick', new Date())
            }
          },
          {
            text: '一小时前',
            onClick (picker) {
              const date = new Date()
              date.setTime(date.getTime() - 3600 * 1000)
              picker.$emit('pick', date)
            }
          },
          {
            text: '昨天',
            onClick (picker) {
              const date = new Date()
              date.setTime(date.getTime() - 3600 * 1000 * 24)
              picker.$emit('pick', date)
            }
          }
        ]
      },
      dateArr: [
        this.moment().add(-7, 'day').startOf('day').format('YYYY-MM-DD'),
        this.moment().endOf('day').format('YYYY-MM-DD')
      ],
      table: {
        data: []
      }
    }
  },
  mounted () {
    this.handleQuery()
  },
  methods: {
    color (item) {
      return {
        color: item.color,
        'border-color': item.color
      }
    },
    summary ({ data }) {
      let sum = 0
      data.forEach(e => {
        if (e.type) {
          sum += e.money || 0
        } else {
          sum -= e.money || 0
        }
      })
      return ['汇总', '汇总', '汇总', sum, '汇总', '汇总']
    },
    handleChange (e) {
      if (e) {
        this.submitForm.date = this.moment(e).format('YYYY-MM-DD')
      }
    },
    async handleQuery () {
      const res = await getIncomeList(this.queryForm)
      if (res) {
        this.table.data = res
      }
    },
    handleDate (e) {
      if (e && e.length === 2) {
        this.queryForm.startDate = this.moment(e[0]).startOf('day').format('YYYY-MM-DD')
        this.queryForm.endDate = this.moment(e[0]).endOf('day').format('YYYY-MM-DD')
      }
    },
    handleAdd () {
      this.dialog = {
        title: '新增',
        show: true
      }
      this.submitForm = {
        date: '',
        time: '',
        money: 0.00,
        type: 0,
        tags: [],
        description: ''
      }
    },
    handleEdit (row) {
      this.dialog = {
        title: '编辑',
        show: true
      }
      this.submitForm = {
        ...(row || {})
      }
    },
    async handleSave () {
      try {
        let res = false
        if (this.submitForm.id) {
          res = await editIncome({}, this.submitForm)
        } else {
          res = await saveIncome({}, this.submitForm)
        }
        if (res) {
          this.$message.success(`${this.dialog.title}成功`)
        } else {
          this.$message.error(`${this.dialog.title}失败`)
        }
        this.dialog.show = false
      } catch (e) {
        this.$message.error(`${this.dialog.title}失败`)
        this.dialog.show = false
      }
    },
    async handleDelete (id) {
      try {
        const res = await deleteIncome({id})
        if (res) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      } catch (e) {
        this.$message.error('删除失败')
      }
    }

  },
  computed: {}
}
</script>

<style scoped>

</style>
