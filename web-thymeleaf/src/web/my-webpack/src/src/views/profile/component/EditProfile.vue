<template>
  <div class="info-box">
    <profile-head content="我的信息"></profile-head>
    <el-form class="user-form" ref="form" :model="userInfo" :rules="formRules" label-width="80px">
      <el-form-item label="昵称:" prop="nickName">
          <el-input :maxlength="10" show-word-limit v-model.trim="userInfo.nickName" style="width:250px"></el-input>
          <span class="tip">注：修改一次需要消耗5B币</span>
      </el-form-item>
      <el-form-item label="用户名:">
          <span class="tip">{{userName}}</span>
      </el-form-item>
      <el-form-item label="我的签名:" prop="signature">
        <el-input type="textarea" :rows="4"  resize="none" :maxlength="100" show-word-limit placeholder="这个人懒死了什么都没写" v-model="userInfo.signature"></el-input>
      </el-form-item>
      <el-form-item label="性别:" prop="sex">
        <el-radio-group v-model="userInfo.sex" >
          <el-radio-button :label="1">男</el-radio-button>
          <el-radio-button :label="0">女</el-radio-button>
          <el-radio-button :label="-1">保密</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="出生日期:" prop="birthday">
        <el-date-picker
          v-model="userInfo.birthday"
          type="date"
          format="yyyy-MM-dd"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-divider></el-divider>
      <div style="text-align:center;">
        <el-button type="primary" @click="saveUserInfo">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import ProfileHead from './ProfileHead'
export default {
  components: {ProfileHead},
  data() {
    let validateLength = (rule, value, callback) => {
      let min = rule.min || 0
      let max = rule.max || 9999999
      if (value.length < min || value.length > max) {
        callback(new Error())
      }
      callback()
    };
    return {
      formRules: {
        nickName: [{
          min: 1, 
          max: 10,
          validator: validateLength,
          message: "长度在1~10个字符",
          trigger: 'blur'
        }],
        signature: [{
          max: 100,
          validator: validateLength,
          message: "长度在100个字符以内",
          trigger: 'blur'
        }]
      },
      userName: "bili_" + this.$store.state.profile.userInfo.uaId,
      userInfo: {
        nickName: this.$store.getters.nickName,
        sex: this.$store.state.profile.userInfo.sex,
        signature: this.$store.state.profile.userInfo.signature || '',
        birthday: this.$store.state.profile.userInfo.birthday
      },
      oldUserInfo: this.$store.state.profile.userInfo
    }
  },
  methods: {
    saveUserInfo() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          let updateUserInfo = {}
          if (this.oldUserInfo.nickName != this.userInfo.nickName) {
            updateUserInfo.nickName = this.userInfo.nickName
          }
          if (this.oldUserInfo.sex != this.userInfo.sex) {
            updateUserInfo.sex = this.userInfo.sex
          }
          if (this.oldUserInfo.signature != this.userInfo.signature) {
            updateUserInfo.signature = this.userInfo.signature
          }
          if (this.oldUserInfo.birthday != this.userInfo.birthday) {
            updateUserInfo.birthday = this.userInfo.birthday
          }
          console.log(updateUserInfo)
          this.$axios.post("/user/account/updateUserInfo", updateUserInfo)
          .then(resp => {
            console.log(resp.data)
          }).catch(e => {
            console.log(e)
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      })
      this.$refs['form'].resetFields()
    }
  },
  mounted: function() {
    this.$nextTick(function () {
      this.$axios.get("/user/account/getUserInfo")
      .then(resp => {
        this.oldUserInfo = resp.data.data
        //设置默认值
        this.userInfo.nickName = this.oldUserInfo.nickName
        this.userInfo.signature = this.oldUserInfo.signature || ''
        this.userInfo.sex = this.oldUserInfo.sex
        this.userInfo.birthday = this.oldUserInfo.birthday
      })
      .catch (e => {
        console.log(e)
      })
    })
  }

}
</script>
<style scoped>
.user-form {
  margin-left: 10px;
  text-align: left;
}
.tip {
  color: rgb(153, 153, 153)
}
</style>