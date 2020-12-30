<template>
    <el-card>
      <el-steps :active="active" align-center finish-status="success" process-status="process">
          <el-step title="实名认证"
            :icon="icons[active][0]"
          ></el-step>
          <el-step title="实名认证" 
          :icon="icons[active][1]"
          ></el-step>
          <el-step title="安全绑定" 
          :icon="icons[active][2]"
          ></el-step>
      </el-steps>
        <el-button v-if="active > 0" @click="last">上一步</el-button>
        <el-button class="" @click="next">{{tipNext}}</el-button>
    </el-card>
</template>

<script>
export default {
    data() {
        return {
            active: 0,
            tipNext: "下一步",
            icons: [["el-icon-edit-outline", "el-icon-warning-outline", "el-icon-warning-outline"], ["", "el-icon-edit-outline", "el-icon-warning-outline"], ["", "", "el-icon-edit-outline"], ["", "", ""]]
        }
    },
    methods: {
        next() {
            this.active++
            if (this.active == 2) {
              this.tipNext = "完成"
            } else if (this.active >= 3) {
              this.tipNext = "下一步"
              this.active = 0
            }
        },
        last() {
          this.active --
        }
    }
}
</script>

<style scoped>

</style>