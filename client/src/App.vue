<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  watch: {
    $route: {
      immediate: true,
      handler (val) {
        if (val.meta && val.meta.title) {
          document.title = '股票分析 - ' + val.meta.title
        }
      }
    }
  }
}
</script>
<style lang="scss">
  @import "theme/index.scss";
  @import "theme/element-ui.scss";
</style>
