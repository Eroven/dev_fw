<template>
  <div id="app">
    <router-view />
    <!-- <foot-copyright/> -->
    <el-backtop></el-backtop>
  </div>
</template>

<script>
import FootCopyright from './components/FootCopyright.vue'
export default {
  components: { FootCopyright },
  name: 'App'
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  /* background-color: #e2ee76; */
  height: 100%;
  width: 100%;
}

</style>
