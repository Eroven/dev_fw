<template>
  <div>
    <top-banner></top-banner>
    <div class="box-content">
      <el-card class="box-card">
        <el-row>
          <el-col :span="4">
            <el-tabs tab-position="left" v-model="cardVal" @tab-click="handClick">
              <el-tab-pane name="ProfileHome">
                <span slot="label"><i class="el-icon-house"></i>首页</span>
              </el-tab-pane>
              <el-tab-pane name="BigVip">
                <span slot="label"><i class="el-icon-pear">大会员</i></span>
              </el-tab-pane>
              <el-tab-pane name="EditProfile">
                <span slot="label"><i class="el-icon-user">我的信息</i></span>
              </el-tab-pane>
              <el-tab-pane name="EditProfilePhoto">
                <span slot="label"><i class="el-icon-camera">我的头像</i></span>
              </el-tab-pane>
            </el-tabs>
          </el-col>
          <el-col :span="20">
            <div>
              <router-view />
            </div>
          </el-col>
         </el-row>
      </el-card>
    </div>
  </div>
</template>
<script>
import TopBanner from '@/components/TopBanner'
export default {
  components: { TopBanner},
  data() {
    return {
      cardVal: this.$route.name || "EditProfile"
    }
  },
  computed: {
    routerKey() {
      return "/Profile/" + this.cardVal
    }
  },
  methods: {
    handClick(tab, event) {
      let to = this.routerKey
      if (this.$route.path != to) {
        this.$router.push(to)
      }

    }
  },
  mounted: function() {
    this.cardVal = this.$route.name || "EditProfile"
  },
  watch: {
  }
}
</script>
<style scoped>
.box-content {
  width: 100%;
  text-align: center;
}
.box-card {
  margin: 0px auto;
  width: 860px;
  height: 980px;
}
</style>