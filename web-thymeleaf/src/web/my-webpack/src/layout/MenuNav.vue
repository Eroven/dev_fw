<template>
  <el-header>
    <el-menu
      :default-active="activeIndex"
      mode="horizontal"
      @select="handleSelect"
      router
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-menu-item index="HelloUser"
        > 处理中心 </el-menu-item
      >
      <el-submenu index="2"  :show-timeout="1">
        <template slot="title">我的练习册</template>
        <el-menu-item index="FileUpload">文件上传</el-menu-item>
        <el-menu-item index="CompleteProfile">完善个人信息</el-menu-item>
        <el-menu-item index="2-3">选项3</el-menu-item>
        <el-submenu index="2-4">
          <template slot="title">选项4</template>
          <el-menu-item index="2-4-1">选项1</el-menu-item>
          <el-menu-item index="2-4-2">选项2</el-menu-item>
          <el-menu-item index="2-4-3">选项3</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-menu-item index="OrderManage"
        > 订单管理 </el-menu-item
      >
    </el-menu>
    <el-dropdown class="user-div" size="medium" v-bind:show-timeout="dropShowTimeout" @command="handleCommand">
      <span class="el-dropdown-link el-icon-user-solid" >
        {{showName}}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="HelloUser">螺蛳粉</el-dropdown-item>
        <el-dropdown-item divided command="Logout"><span class="el-icon-switch-button logout">&nbsp;</span>注&nbsp;销</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-header>
</template>

<script>
export default {
  data: function() {
    return {
      dropShowTimeout: 10
      // showName: this.$store.getters.profile.sub
    }
  },
  computed: {
    showName () {
      return this.$store.getters.nickName
    },
    activeIndex () {
      return this.$route.name
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      // console.log("sub: " + this.$store.getters.nickName)
      // console.log(key, keyPath)
    },
    handleCommand(command) {
      // console.log("command:" + command)
      switch(command) {
        case "Logout": 

          this.$router.push({name: "Login"})
        break;
      }
    }
  }
}
</script>

<style scoped>
.user-div {
  position: fixed;
  right: 50px;
  top: 20px;
  color:rgb(255, 255, 255)

}
 .el-dropdown-link {
    cursor: pointer;
    color:rgb(255, 255, 255);
  }
  .el-icon-arrow-down {
    font-size: 14px;
  }
  .logout {
    color: rgb(241, 41, 74);
  }
</style>