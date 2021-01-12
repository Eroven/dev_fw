<template>
  <div class="foot">
    <p id="contact">xxx©2021</p>
  </div>
</template>

<script>
export default {
  name: 'FootCopyright',
  data () {
    return {
      msg: '请登录'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.foot {
  margin-bottom: 30px;
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translate(-50%, 0%);
}
#contact {
  color: white;
}
</style>
