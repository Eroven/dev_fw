<template>
    <el-container>
        <el-header>
          <menu-nav></menu-nav>
        </el-header>
      <el-main>
        <content-main></content-main>
      </el-main>
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
.el-main {
  text-align: center;
  margin-top: 55px;
  padding: 0px;
}
.el-header {
  text-align: center;
  line-height: 0px;
  min-height: 100px;
  padding: 0px;
}

</style>