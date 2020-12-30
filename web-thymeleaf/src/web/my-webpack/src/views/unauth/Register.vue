<template>
  <div>
    <div class="top-banner">
      <img src="../../assets/top.png" />
    </div>
    <div>
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="4"><span>&nbsp;</span></el-col>
        <el-col :span="16" style="">
          <el-divider content-position="center" ><span class="tip">注册</span></el-divider>
        </el-col>
        <el-col :span="4"></el-col>
      </el-row>
    </div>
    <div class="register-div">
      <el-form 
      :v-model="regForm" 
      :rules="rules" 
      ref="regForm" 
      >
        <el-form-item prop="nickName">
          <el-input type="text" v-model="regForm.nickName" placeholder="昵称" clearable  maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="regForm.password" placeholder="密码" show-password></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input type="text" v-model="regForm.email" placeholder="邮箱" clearable></el-input>
        </el-form-item>
        <el-form-item prop="authCode">
          <el-input type="text" v-model="regForm.authCode" placeholder="验证码">
            <el-button slot="suffix" size="medium" type="primary" :disabled="getCode" class="code-btn" @click="getAuthCode">点击获取</el-button>
          </el-input>
        </el-form-item>
        <el-form-item prop="agree">
          <el-checkbox v-model="regForm.agree" @change="changeAgree">我已同意<el-link type="primary">《哔哩哔哩弹幕网用户使用协议》</el-link>和<el-link type="primary">《哔哩哔哩隐私政策》</el-link></el-checkbox><br>
          <el-button style="width:420px" :type="regType" @click="onSubmit" :disabled="!regForm.agree">注册</el-button>
        </el-form-item>
         <span class="to-login" @click="routeLogin">已有账号,直接登录&gt;</span>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      regForm: {
        nickName: '',
        password: '',
        email: '',
        authCode: '',
        agree: false
      },
      regType: '',
      getCode: true,
      rules: {
        nickName: [{ validator: this.checkNickName, trigger: "blur", message: "请告诉我你的昵称吧" }],
        password: [{ validator: this.checkPass, trigger: "blur", message: "请输入密码" }],
        email: [{ validator: this.checkEmail, trigger: "change", message: "请输入邮箱" }],
        authCode: [{ validator: this.checkCode, trigger: "blur", message: "请输入验证码" }]
        
      }
    }
  },
  methods: {
    changeAgree(){
      if (this.regForm.agree) {
        this.regType = "primary"
      } else {
        this.regType = ""
      }
    },
    checkNickName(rule, value, callback) {
      console.log(this.regForm.nickName)
      if (!this.regForm.nickName)
        callback(new Error())
      else callback()
    },
    checkPass(rule, value, callback) {
      if (!this.regForm.password)
        callback(new Error())
      else callback()
    },
    checkEmail(rule, value, callback) {
      if (!this.regForm.email){
        this.getCode = true
        callback(new Error())
      }
      else {
        this.getCode = false
        callback()
      }
    },
    checkCode(rule, value, callback) {
      if (!this.regForm.authCode)
        callback(new Error())
      else callback()
    },
    getAuthCode(){
      this.$axios.post("/unAuth/authCode", {'email': this.regForm.email })
    },
    onSubmit() {
      //  this.$refs["regForm"].validate((valid) => {
          // if (valid) {
            this.$axios.post("/unAuth/register", {
              nickName: this.regForm.nickName,
              password: this.regForm.password,
              email: this.regForm.email,
              authCode: this.regForm.authCode
              })
          // } else {
            // console.log('error submit!!');
            // return false;
          // }
        // });
    },
    routeLogin() {
      this.$router.push("/Login")
    }
  }
}
</script>

<style scoped>
.top-banner {
  background: #00a0d8;
  height: 86px;
  text-align: center;
  margin-bottom: 40px;
}
.tip {
  font-size: 38px;
}

.register-div {
  width: 420px;
  left: 50%;
  margin-left: -260px;
  text-align: center;
  position: fixed;
  padding: 60px 0 0 60px

}

.code-btn {
  position: absolute;
  left: -143px;
  margin-top: 2px;
  width: 146px;
}

.to-login {
  font-size: 12px;
  color: #00a0d8;
  cursor: pointer;
  top: -10px;
  position: relative;
  left: 155px;
}
</style>