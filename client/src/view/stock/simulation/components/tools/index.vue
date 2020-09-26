<template>
  <div style="height: 100%">
    <div style="height: 100%" @contextmenu.prevent="handleMenu" @click="show = false">
      <slot></slot>
    </div>
    <div class="tools-wrap" v-show="show" :style="styleObj">
      <el-form label-width="130px">
        <el-form-item label="日期">
          <el-input :value="submitForm.date" size="mini" readonly></el-input>
        </el-form-item>
        <el-form-item label="剩余资金">
          <el-input :value="submitForm.holdMoney" size="mini" readonly></el-input>
        </el-form-item>
        <el-form-item label="持有股票">
          <el-input :value="submitForm.holdVolume" size="mini" readonly></el-input>
        </el-form-item>
        <el-form-item label="持有股票每股价格">
          <el-input :value="submitForm.originMoney" size="mini" readonly></el-input>
        </el-form-item>
        <el-form-item label="当前价格">
          <el-input :value="submitForm.price" size="mini" readonly></el-input>
        </el-form-item>
        <el-form-item label="机器建议">
          <el-input size="mini" readonly :value="suggest"></el-input>
        </el-form-item>
        <el-form-item label="买卖类型">
          <el-select v-model="submitForm.operate" size="mini">
            <el-option label="购买" value="buy"></el-option>
            <el-option label="出售" value="sale"></el-option>
            <el-option label="不处理" value="nothing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="买卖股票数量" v-if="submitForm.operate !== 'nothing'">
          <el-input v-model="submitForm.volume" size="mini">
            <span slot="suffix">00</span>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="next" type="primary">下一天</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    value: {
      type: Object,
      default: () => ({})
    },
    suggest: {
      type: String,
      default: ''
    }
  },
  data: () => ({
    x: 0,
    y: 0,
    show: false,
    submitForm: {
      holdMoney: 0,
      holdVolume: 0,
      date: '',
      originMoney: 0,
      price: 0,
      volume: 0,
      operate: 'nothing'
    }
  }),
  mounted () {
  },
  methods: {
    handleMenu (e) {
      this.show = true
      this.x = e.screenX
      this.y = e.screenY
    },
    next () {
      this.$emit('next')
    }
  },
  watch: {
    value: {
      immediate: true,
      deep: true,
      handler (val) {
        if (val) {
          this.submitForm = val
        }
      }
    },
    submitForm: {
      deep: true,
      handler (val) {
        this.$emit('change', val)
      }
    }
  },
  computed: {
    styleObj () {
      return {
        top: (this.y - 100) + 'px',
        left: this.x + 'px'
      }
    }
  }
}
</script>
<style scoped>
  .tools-wrap {
    width: 300px;
    padding: 20px;
    position: fixed;
    background-color: white;
    z-index: 999;
    box-shadow: 1px 2px 12px 0 rgba(0,0,0,.1);
  }
</style>
