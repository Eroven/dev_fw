<template>
  <el-container>  
    <!-- <el-aside width="200px">Aside</el-aside> -->
    <el-container>
      <el-header>
        <menu-nav></menu-nav>
      </el-header>
      <el-main>
        <content-main></content-main>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import MenuNav from "./MenuNav"
import ContentMain from "./ContentMain"
export default {
  components: {MenuNav, ContentMain}
}
</script>

<style scoped>
.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  /* line-height: 200px; */
}
.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  /* line-height: 160px; */
}
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  /* line-height: 60px; */
  padding: 0px;
}

</style>