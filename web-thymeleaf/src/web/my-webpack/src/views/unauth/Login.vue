<template>
  <div>
    <div class="top-banner">
      <img src="../../assets/top.png" />
    </div>
    <el-row type="flex" justify="center" align="middle">
      <el-col :span="3"><div>&nbsp;</div></el-col>
      <el-col :span="18" style="">
        <el-divider content-position="center" ><span class="tip">登录</span></el-divider>
      </el-col>
      <el-col :span="3"><div>&nbsp;</div></el-col>
    </el-row>
    <el-row class="mid-container" type="flex" justify="center">
      <el-col :span="3"><div>&nbsp;</div></el-col>
      <el-col :span="8" >
        <img src="../../assets/login-img.png" />
      </el-col>
      <el-col :span="2">
        <div class="mid-divider">
          <el-divider direction="vertical" class="mid-divider"></el-divider>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="login-data">
            <el-form
              :model="ruleForm"
              status-icon
              :rules="rules"
              ref="ruleForm"
            >
              <el-form-item  prop="email">
                <el-input
                  type="text"
                  v-model="ruleForm.email"
                  autocomplete="off"
                  placeholder="邮箱"
                  :disabled="login"
                ></el-input>
              </el-form-item>
              <el-form-item prop="pass">
                <el-input
                  type="password"
                  v-model="ruleForm.pass"
                  autocomplete="off"
                  placeholder="密码"
                  show-password
                  :disabled="login"
                ></el-input>
              </el-form-item>
              <el-form-item >
                <div class="remember-me">
                  <el-checkbox v-model="rememberMe"  @change="changeRememberMe">记住我</el-checkbox>
                  <span>不是自己的电脑请不要勾选此项</span>
                </div>
              </el-form-item>
              <el-form-item >
                <el-button class="btn" v-if="!login" type="primary" @click="submitForm('ruleForm')"
                  >登录</el-button
                >
                <el-button class="btn" v-if="login" type="primary" :loading="true">登录中</el-button>
                <el-button class="btn" type="" @click="routeRegister">注册</el-button>
              </el-form-item>
            </el-form>
          </div>
      </el-col>
      <el-col :span="3"><div>&nbsp;</div></el-col>
    </el-row>
</div>
</template>

<script>
import Cache from '@/utils/Cache'
import {parseToken} from '@/utils/Parser'
export default {
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    }
    let validateUser = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入邮箱"));
      } else {
        callback();
      }
    }
    return {
      ruleForm: {
        email: '',
        pass: ''
      },
      rules: {
        pass: [{ validator: validatePass, trigger: "blur" }],
        email: [{ validator: validateUser, trigger: "blur" }]
      },
      login: false,
      rememberMe: Cache.isRememberMe() ? true : false
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.login = true
          this.$axios.post("/unAuth/login", {'email': this.ruleForm.email, 'password': this.ruleForm.pass})
                .then(resp => {
                  let data = resp.data
                  if ("200" === data.status) {
                    let token = data.data
                    Cache.setToken(token)
                    this.$store.commit('setToken',token)
                    let profile = parseToken(token)
                    this.$store.commit('setProfile', profile)
                    // this.$store.commit('setNickName', profile.sub)
                    console.log("login -> profile:")
                    console.log(profile)
                    console.log(this.$store.getters.profile)
                    console.log(this.$store.getters.nickName)
                    console.log(this.$store.state.token)
                    this.$router.push("/")
                  } else {
                    this.$message({
                      showClose: true,
                      message: '用户名或密码错误',
                      type: 'error'
                    });
                  }
                }).finally(() => this.login = false)
        } else {
          console.log("error submit!!");
          return false;
        }
      })
    },
    changeRememberMe(checked) {
      console.log("remember me : " + checked)
      Cache.rememberMe(checked)
    },
    routeRegister(){
      this.$router.push("/Register")
    }
   
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
.mid-container {
  height: 460px;
  margin-top: 20px;
}
.mid-divider {
  height: 460px;
  text-align: center;
}

.login-data {
  text-align: center;
  height: 100%;
  width: 100%;
  margin-top: 100px;
}
.remember-me {
  text-align: left;
  position: relative;
  top: 18px;
}
.remember-me span{
  color: #bbb;
  margin-left: 10px;
}
.btn {
  width: 265px;
}
</style>
