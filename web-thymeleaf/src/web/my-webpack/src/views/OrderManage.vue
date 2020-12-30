<template>
    <el-card>
        <div style="text-align:left">
            <span>请输入数量：</span>
            <el-input-number v-model="num" controls-position="right" :min="1" :max="100"></el-input-number>
            <el-button type="primary" @click="getOrders">确认</el-button>
        </div>
        <el-table :data="dataList" stripe >
            <el-table-column prop="date|dateFormate" :formatter="dateFormat" label="日期" width="240"></el-table-column>
            <el-table-column prop="name" label="姓名" width="220"></el-table-column>
            <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
    </el-card>
</template>

<script>
import moment from 'moment'
export default {
    name: 'OrderManage',
    data () {
       return {
           num: 10,
           dataList: []
       }
    },
    methods: {
        getOrders() {
            let vm = this
            this.$axios.get('/test/orders?size=' + this.num)
            .then(function(resp){
                vm.dataList = resp.data.data
            })
        },
        dateFormat(row,col,val,idx) {
            return moment(val).format("YYYY-MM-DD HH:mm:ss")
        }
    },
    filters: {
        
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .el-card {
        cursor: pointer;
    }

</style>
