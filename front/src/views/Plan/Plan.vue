<template>
  <div id="Plan">
    <h1>生产计划单处理</h1>
    <el-form :inline="true" label-width="90px">
      <search ref="soushuo">
        <el-form-item label="计划单单号">
          <el-input type="text" v-model="planNo"></el-input>
        </el-form-item>
        <el-form-item label="需求单单号">
          <el-input type="text" v-model="formNo"></el-input>
        </el-form-item>
        <el-form-item label="生产线">
          <el-input type="text" v-model="productionLineId"></el-input>
        </el-form-item>
        <el-button @click="find()" slot="button">搜索</el-button>
      </search>
    </el-form>
    <div>

      <el-table :data="result" border style="margin-left:50px;width:1270px">
        <el-table-column prop="no" label="计划单单号"></el-table-column>
        <el-table-column prop="demandForm.no" label="需求单单号"></el-table-column>
        <el-table-column prop="distributeTime" label="分配时间" :formatter="timeFormat"></el-table-column>
        <el-table-column prop="productionLine.workshopName" label="车间"></el-table-column>
        <el-table-column prop="productionLine.name" label="生产线"></el-table-column>
        
        <el-table-column label="操作">
          <template slot-scope="scope">
              <el-button v-if="!scope.row.maSet[0]" @click="apply(scope.row.id)">
                申请
              </el-button>

              <el-button @click="detail(scope.row.id)">详情</el-button>
            
          </template>  
        </el-table-column>
      </el-table>

      
    </div>
    <div style="margin:0 auto;">
      <page-bar :all="all" :page="page"></page-bar>
    </div>
  </div>
</template>

<script>
import request from "../../network/Plan/CommondForm.js";
import Search from "../../components/Plan/search/Search.vue";
import PageBar from "../../components/Plan/pageBar/PageBar.vue";
import { user } from "../../network/Plan/variable.js";

export default {
  components: {
    Search,
    PageBar,
  },
  name: "Plan",
  data() {
    return {
      result: {},
      planNo: "",
      productionLineId: "",
      all: 10,
      page: 1,
      begintime: "",
      overtime: "",
      formNo: "",
      te: {
        id: [],
        no: "asd",
      },
    };
  },
  created() {
    this.find()
  },
  activated() {
    console.log("activated调用了");
    request({
      url: "/plan/getPlanPageByCriteria",
      params:{
        pageSize: 10,
        company_id: this.$store.getters.userinfo.company_id
      }
    })
      .then((res) => {
        let result = res.data.result;
        this.result = result.records;
        this.page = result.current;
        this.all = result.pages;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    timeFormat(row,column){
      if(row.distributeTime){
        return row.distributeTime.slice(0,10)
      }else{
        return row.distributeTime
      }
      },
    detail(val) {
      this.$router.push({
        path:'/process/'+val,
      })
    },
    apply(val) {
      if (!confirm("确认申请物料？")) return;

      let date = new Date();
      request({
        url: "/plan/applyMaterial",
        params: {
          id: val,
          createdBy: user,
          dateStr:
            date.getFullYear() +
            (date.getMonth() + 1).toString().padStart(2, "0") +
            date.getDate().toString().padStart(2, "0"),
        },
      })
        .then((res) => {
          // this.$router.go(0)
          request({
            url: "/plan/getPlanPageByCriteria",
            params: {
              beginTimestamp: !this.begintime
                ? null
                : this.begintime + " 08:00:00",
              endTimestamp: !this.overtime ? null : this.overtime + " 08:00:00",
              no: !this.planNo ? null : this.planNo.trim(),
              "productionLine.id": !this.productionLineId
                ? null
                : this.productionLineI.trim(),
              pageNo: this.page,
              "demandForm.no": !this.formNo ? null : this.formNo.trim(),
              pageSize: 10
            },
          })
            .then((res) => {
              console.log(res);
              let result = res.data.result;
              this.result = result.records;
              this.page = result.current;
              this.all = result.pages;
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async find() {
      request({
        url: "/plan/getPlanPageByCriteria",
        params: {
          beginTimestamp: !this.begintime ? null : this.begintime + " 08:00:00",
          endTimestamp: !this.overtime ? null : this.overtime + " 08:00:00",
          no: !this.planNo ? null : this.planNo.trim(),
          "productionLine.id": !this.productionLineId
            ? null
            : this.productionLineId.trim(),
          pageNo: this.page,
          "demandForm.no": !this.formNo ? null : this.formNo.trim(),
          pageSize: 10
        },
      })
        .then((res) => {
          console.log(res);
          let result = res.data.result;
          this.result = result.records;
          this.page = result.current;
          this.all = result.pages;
        })
        .catch((err) => {
          console.log(err);
        });

      // console.log(this.result);
    },
    changePage(val) {
      this.page = val;
    },
    changeAll(val) {
      this.all = val;
    },
  },
};
</script>

<style>

</style>

