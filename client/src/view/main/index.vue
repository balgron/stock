<template>
  <el-container>
    <el-header height="40px"></el-header>
    <el-container>
      <el-aside width="250px">
        <div class="aside">
          <nav-bar></nav-bar>
        </div>
      </el-aside>
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>

import NavBar from './nav'
import {mapActions} from 'vuex'
export default {
  components: {
    NavBar
  },
  data: () => ({}),
  mounted () {
    this.getStock()
  },
  methods: {
    ...mapActions(['getStock'])
  },
  computed: {

  }
}
</script>

<style lang="scss">
  @import "index.scss";
</style>
