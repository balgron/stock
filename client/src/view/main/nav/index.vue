<template>
  <el-menu class="el-menu-vertical-demo" :default-active="active" :collapse="closeNav" router unique-opened>
    <el-submenu v-for="(route, i) in routers" :index="route.path" :key="i">
      <div slot="title">
        <i :class="[route.icon]" v-if="route.icon"></i>
        <span>{{route.title}}</span>
      </div>
      <template v-if="route.children && route.children.length > 0">
        <template v-for="(r, j) in route.children" >
          <el-submenu v-if="r.children && r.children.length > 0" :key="j" :index="r.path">
            <div slot="title">
              <i :class="[r.icon]" v-if="r.icon"></i>
              <span>{{r.title}}</span>
            </div>
            <template>
              <el-menu-item  v-for="(rr, k) in r.children" :key="k" :index="rr.path">{{rr.title}}</el-menu-item>
            </template>
          </el-submenu>
          <el-menu-item :key="j" :index="r.path" v-else>{{r.title}}</el-menu-item>
        </template>
      </template>
    </el-submenu>
  </el-menu>
</template>

<script>
import {mapGetters} from 'vuex'
import {routers} from '@/router'
export default {
  data: () => ({
    active: ''
  }),
  watch: {
    $route: {
      immediate: true,
      handler (val) {
        this.active = val.path
      }
    }
  },
  computed: {
    ...mapGetters(['closeNav']),
    routers () {
      const router = routers.filter(e => !e.hidden)
      router.forEach(e => {
        if (e.children) {
          e.children = e.children.filter(i => !i.hidden)
        }
      })
      return router
    }
  }
}
</script>

<style scoped>

</style>
