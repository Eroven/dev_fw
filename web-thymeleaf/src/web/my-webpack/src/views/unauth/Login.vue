<template>
  <div>
    <div class="top-banner">
      <img src="../../assets/top.png" />
    </div>
    <div class="middle">
        <el-divider content-position="center" ><span class="tip">登录</span></el-divider>
    </div>
    <div class="login-box">
      <div class="login-left">
        <img  class="login-img" src="../../assets/login-img.png" />
      </div>
      <div>
        <el-divider direction="vertical" class="mid-divider"></el-divider>
      </div>
      <div class="login-right">
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
      </div>
    </div>
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
                    if (this.rememberMe) {
                      Cache.rememberToken(token)
                    }
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
          this.$message({
                      showClose: true,
                      message: '请填写相关信息',
                      type: 'warning'
                    });
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
.middle {
    width: 980px;
    margin: 0px auto;
}
.login-box {
  position: relative;
  display: flex;
  width: 980px;
  margin: 40px auto;
}
.log-left {
  padding: 10px;
  width: 489px;
}
.login-img {
  width: 480px;
}
.mid-divider {
  height: 400px;
  text-align: center;
}
.login-right {
  margin-left: 40px;
  width: 460px;
}
.login-data {
  text-align: center;
  width: 400px;
  margin-top: 100px;
}

.mid-container {
  height: 460px;
  min-width: 1080px;
  margin-top: 20px;
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
  float: left;
  width: 190px;
}
</style>
