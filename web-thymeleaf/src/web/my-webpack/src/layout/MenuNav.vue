<template>
  <div class="header">
    <el-row type="flex" justify="start">
      <el-col :span="10">
        <ul class="header-link">
          <li @click="link('/HelloUser')">
            <span class="el-icon-grape">主站</span>
          </li>
          <li>
            <span>番剧</span>
          </li>
          <li>
            <span>游戏中心</span>
          </li>
          <li>
            <span>直播</span>
          </li>
          <li>
            <span>会员购</span>
          </li>
          <li>
            <span>漫画</span>
          </li>
          <li>
            <span>赛事</span>
          </li>
          <li>
            <span class="el-icon-download">下载APP</span>
          </li>
        </ul>
      </el-col>
      <el-col :span="4">
        <div class="header-search">
          <el-input placeholder="请输入内容" v-model="search" class="input-with-select">
            <el-button slot="append" icon="el-icon-search"></el-button>
          </el-input>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="header-user">
          <div class="item">
            <div>
              <el-button class="item-upload" size="medium" type="danger" round>投稿</el-button>
            </div>
          </div>
          <div class="item">
            <span>创作中心</span>
          </div>
          <div class="item">
            <span>历史</span>
          </div>
          <div class="item">
            <div class="item-badge">
                <el-badge :value="12">
                  <span>动态</span>
                </el-badge>
            </div>
          </div>
          <div class="item">
            <div class="item-badge">
              <div>
                <el-badge :value="3" >
                  <span>消息</span>
                </el-badge>
              </div>
            </div>
          </div>
          <div class="item">
            <span>大会员</span>
          </div>
          <div class="item item-photo"  >
            <transition name="slide-fade">
                <div v-if="!showList" @mouseenter="dropdownList">
                  <el-avatar :size="photoSize" :src="photoUrl"  ></el-avatar>
                </div>
            </transition>
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="layer">
      <img src="../assets/title_2.jpg" style="transform: scale(1) translate(0px) rotate(0deg); filter: blur(0px); opacity: 1;" width="2016" height="155">
    </div>
    <div class="layer layer-bg"></div>
    <transition name="slide-fade">
      <div v-if="showList" @mouseleave="pickupList" class="user-menu">
        <el-avatar class="item-photo-big" :size="photoSize" :src="photoUrl" ></el-avatar>
        <div class="user-menu-list">
          <p class="show-name">{{showName}}</p>
          <div>
            <span style="float:left;">等级{{level}}</span>
            <span style="float:right;">{{currentExp}}/{{maxExp}}</span>
            <el-progress :percentage="currentExp/maxExp*100" :format="expFormat"></el-progress>
          </div>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="8">关注：73</el-col>
            <el-col :span="8">粉丝：8</el-col>
            <el-col :span="8">动态: 1</el-col>
          </el-row>
          <el-divider class="menu-divider"></el-divider>
          <div class="menu-item"><span @click="link('/Profile')" class="el-icon-s-custom">个人中心</span></div>
          <div class="menu-item"><span class="el-icon-upload2">投稿管理</span></div>
          <div class="menu-item"><span class="el-icon-coin">B币钱包</span></div>
          <el-divider  class="menu-divider"></el-divider>
          <div class="menu-item" @click="exit"><i class="el-icon-switch-button" ></i>退出</div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Cache from '@/utils/Cache'
export default {
  data: function() {
    return {
      search: '',
      photoSize: 'medium',
      photoUrl: "",
      dropShowTimeout: 10,
      showList: false,
      level: 5,
      currentExp: 16292,
      maxExp: 28800
    }
  },
  mounted: function () {
    this.$nextTick(function () {
      this.$axios.get("/file/user/photo").then(resp => {
        let data = resp.data
        this.photoUrl = "data:image/jpeg;base64," + data.data
      })
    })
   
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
    dropdownList() {
      this.showList = true
    },
    pickupList() {
      this.showList = false
    },
    expFormat(percentage) {
        return ""
    },
    link(path) {
      if (this.$route.path != path) {
        this.$router.push(path)
      }
    },
    userAccount() {
      console.log("个人中心")
    },
    exit() {
      console.log("退出")
      this.$store.commit("logout")
      this.$router.push("/Login")
    }
  }
}
</script>

<style scoped>
.header {
  line-height: 50px;
  min-height: 50px;
  text-align: center;
  color: rgb(255, 255, 255);
  font-size: 14px;
  padding: 0;
}
.header-link {
  padding-left: 10px;
  margin: 0px;
  min-width: 400px;
}
ul li{
  list-style-type: none;
  float: left;
  margin-left: 8px;
  cursor: pointer;
}
.header-search {
  width: 180px;
}
.header-user {
  min-width: 400px;
  margin-left: 10px;
  padding-right: 10px;
}
.item {
  float: right;
  margin-left: 18px;
  cursor: pointer;
}
.item-photo {
  margin: 8px auto;
}
.item-photo-big {
  width: 60px;
  height: 60px;
}
.user-menu {
  position: absolute;
  top: 10px;
  right: 300px;
  z-index: 9999999;
  line-height: 40px;
}
.user-menu-list {
  margin-top: -54px;
  padding-top: 20px;
  padding-left: 15px;
  padding-right: 15px;
  width: 240px;
  height: 535px;
  background-color: rgb(255, 255, 255);
  color: black;
}
.show-name {
  display: block;
  color: red;
  font-size: 20px;
  margin-bottom: 0px;
}
.el-progress-bar {
  padding-right: 0px;
  margin-left: 0px;
}
.menu-item {
  font-size: 14px;
  text-align: left;
  padding-left: 10px;
}
.menu-item:hover {
  background-color: rgb(233, 232, 227);
  cursor: pointer;
}
.menu-divider {
  margin-top: 5px;
  margin-bottom: 5px;
}

.item-badge {
  line-height: 20px;
  padding-top: 14px;
}
.item-upload {
  width: 120px;
}
.layer {
  position: absolute;
  left: 0;
  top: 0;
  z-index: -100;
  width: 155;
}
.layer-bg {
  background-color: rgb(88, 41, 41);
  opacity: 0.4;
  width: 100%;
  height: 50px;
}

</style>