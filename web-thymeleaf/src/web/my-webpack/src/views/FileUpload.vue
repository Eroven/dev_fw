<template>
  <div class="upload">
    <el-card>
    <el-upload
      ref="upload"
      class="upload-demo"
      action="/api/file/upload"
      :headers="headerToken"
      :on-preview="handlePreview"
      :on-remove="handleRemove"
      :before-remove="beforeRemove"
      :on-progress="progress"
      :on-success="success"
      :on-error="error"
      multiple
      :limit="3"
      :on-exceed="handleExceed"
      :file-list="fileList">
      <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      <el-button slot="trigger" size="small" type="primary">上传</el-button>
      <el-button size="small" type="warning" @click="clearFiles">取消</el-button>
    </el-upload>
    </el-card>
  </div>
</template>

<script>

  export default {
    data() {
      return {
        fileList: [],
      }
    },
    computed: {
      headerToken () {
        let token = this.$store.state.token
        return { "Authorization" : "Bearer " + token}
      }
    },
    methods: {
      handleRemove(file, fileList) {//文件移除时
        console.log(file, fileList);
      },
      handlePreview(file) {//处理点击已上传的文件的事件
        console.log(`点击：`, file);
      },
      handleExceed(files, fileList) {//超过个数限制
        this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },
      beforeRemove(file, fileList) {//文件移除前
        return this.$confirm(`确定移除 ${ file.name }？`);
      },
      progress(event, file, fileList) {
        // console.log(event)
      },
      success(resp, file, fileList) {
        console.log(resp)
        this.fileList = fileList
      },
      error(error, file, fileList) {
        console.log(error)
        if ("401" == error.status) {
          this.$router.push("Login")
        }
      },
      clearFiles() {
        this.$confirm('删除全部已上传文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.$refs.upload.clearFiles()
          console.log(this.fileList)
          this.fileList = []
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
