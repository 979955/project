<!--主界面的左侧导航菜单栏-->
<!--icon颜色统一为 #bfbfbf-->
<template>
  <div id="NavigationMenu">
    <el-menu
        id="el-menu"
        :router="true"
        :unique-opened="true"
        active-text-color="#ffd04b"
        background-color="#545c64"
        default-active="2"
        text-color="#fff"
        @select="menuSelect">



      <!--田方源 系统管理-用户管理-->
      <!-- 需要拥有子菜单中的任意一项权限，父菜单才会显示， 要不没啥意义-->
      <!-- isValidAccess(数组) : 至少拥有数组中的任意一个权限，返回true-->
      <el-submenu index="system"
                  v-if="isValidAccess(
                      [
                  '公司管理',
                  '系统用户管理',
                  '系统角色管理',
                  '系统部门管理',
                  '系统人员审批',
                  '系统权限管理'])">

        <template slot="title">
          <div class="icon-name-slot">
            <i class="icon-slot">
              <img src="@/assets/icon/system.svg">
            </i>
            <span class="name-slot">系统管理</span>
          </div>
        </template>
        <el-menu-item
            v-if="isValidAccess(['公司管理'])"
            class="double-submenu-item"
            index="/system/companyManagement">公司管理
        </el-menu-item>
        <el-menu-item
            v-if="isValidAccess(['系统用户管理'])"
            class="double-submenu-item"
            index="/system/userManagement">用户管理
        </el-menu-item>
        <el-menu-item
            v-if="isValidAccess(['系统角色管理'])"
            class="double-submenu-item"
            index="/system/roleManagement">角色管理
        </el-menu-item>
        <el-menu-item
            v-if="isValidAccess(['系统部门管理'])"
            class="double-submenu-item"
            index="/system/departmentManagement">部门管理
        </el-menu-item>
        <el-menu-item
            v-if="isValidAccess(['系统人员审批'])"
            class="double-submenu-item"
            index="/system/userApply">人员审批
        </el-menu-item>
        <el-menu-item
            v-if="isValidAccess(['系统权限管理'])"
            class="double-submenu-item"
            index="/system/permissionManagement">权限管理
        </el-menu-item>
      </el-submenu>

      <!--肖雨 产品质量管理      -->
      <el-submenu  v-if="isValidAccess(['质检结果录入','产品瑕疵管理','产品品质管理'])"
                   index="qualityManagement">
        <template slot="title">
          <i class="el-icon-s-order"></i>
          <span>质量管理</span>
        </template>
        <el-menu-item v-if="isValidAccess(['质检结果录入'])"
                      index="/qualityManagement/qualityTaskListComponent">任务清单</el-menu-item>
        <el-menu-item v-if="isValidAccess(['产品瑕疵管理'])"
                      index="/qualityManagement/defectManagementComponent">瑕疵管理</el-menu-item>
        <el-menu-item v-if="isValidAccess(['产品品质管理'])"
                      index="/qualityManagement/qualityManagementComponent">品质管理</el-menu-item>
      </el-submenu>

      <!--陈明 生产过程管理-->
      <el-submenu index="production"
                  v-if="isValidAccess(['生产过程管理'])">
        <template slot="title">
          <i class="el-icon-goods"></i>
          <span>生产过程管理</span>
        </template>
        <el-menu-item index="/process/production/add">产品添加</el-menu-item>
        <el-menu-item index="/process/production/list">产品查看</el-menu-item>
        <el-menu-item index="/process/procedure/add">工序添加</el-menu-item>
        <el-menu-item index="/process/procedure/list">工序查看</el-menu-item>
      </el-submenu>

      <!-- 简绿焜 生产计划管理 -->
      <el-submenu index="plan"  v-if="isValidAccess(['生产计划管理'])">
        <template slot="title">
          <i class="el-icon-goods"></i>
          <span>生产计划管理</span>
        </template>
        <el-menu-item index="/CommondForm">需求单管理</el-menu-item>
        <el-menu-item index="/Plan">计划单管理</el-menu-item>
      </el-submenu>

      <!-- 高志宝 start    -->
      <el-submenu index="workShop" v-if="isValidAccess(['车间管理'])">
        <template slot="title">
          <i class="el-icon-truck"></i>
          <span>车间管理</span>
        </template>
        <el-menu-item index="/work/workshop">车间管理</el-menu-item>
        <el-menu-item index='/work/apply'>申请查询</el-menu-item>
      </el-submenu>
      <el-submenu index="storeHouse" v-if="isValidAccess(['仓库管理'])">
        <template slot="title">
          <i class="el-icon-truck"></i>
          <span>仓库管理</span>
        </template>
        <el-menu-item index="/storehouse/storageMain">库区管理</el-menu-item>
        <el-menu-item index="/storehouse/equAndMatMain">物料设备</el-menu-item>
        <el-menu-item index="/storehouse/inOut">出入库管理</el-menu-item>
        <el-menu-item index="/storehouse/inOutApproval">出入库审批</el-menu-item>
      </el-submenu>

      <!--林志强 数据分析报表-->
      <el-submenu index="dataAnalysis"
                  v-if="isValidAccess(['数据分析报表'])">
        <template slot="title">
          <i class="el-icon-data-analysis"></i>
          <span>数据分析报表</span>
        </template>
        <el-menu-item index="/dataAnalysis/productionSchedule">生产计划进度</el-menu-item>
        <el-menu-item index="/dataAnalysis/materialStock">物料库存</el-menu-item>
        <el-menu-item index="/dataAnalysis/badStatistics">不良统计</el-menu-item>
      </el-submenu>

      <el-submenu index="modelManagement"
                  v-if="isValidAccess(['模板管理'])">
        <template slot="title">
          <i class="el-icon-reading"></i>
          <span>模版管理</span>
        </template>
        <el-menu-item index="/modelManagement/modelManagementComponent">模版管理</el-menu-item>
        <el-menu-item index="/modelManagement/modelAdd">模版添加</el-menu-item>
      </el-submenu>
      <!--   end    -->

    </el-menu>
  </div>
</template>

<script>
export default {
  name: "NavigationMenu",
  data() {
    return {}
  },
  methods: {
    menuSelect(key, keyPath) {
      this.$emit('routes', keyPath);
    },
    isValidAccess(access) {
      // return true;
      // <!-- isValidAccess(数组) : 至少拥有数组中的任意一个权限，返回true-->
      let flag = false;
      try {
        for (let i = 0; i < access.length; i++) {
          let ac = access[i];
          if (this.$store.getters.userinfo.accessList.indexOf(ac) !== -1) {
            flag = true;
            break;
          }
        }
      } catch (e) {
        // this.$message.error("系统内部错误！无用户权限！")
        return false
      }
      return flag
    }
  }
}
</script>

<style scoped>
#NavigationMenu {
  height: 100%;
}
#el-menu {
  width: 100%;
  height: 100%;
  text-align: left;
}
.el-menu-item {
  min-width: auto;
}
.icon-name-slot {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.icon-slot {
}
.name-slot {
  padding-left: 5px;
}
.submenu-item-double {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: flex-start;
}
/*.double-submenu-item {*/
/*  padding: 5px 5px !important;*/
/*  width: 100%;*/
/*  height: 100%;*/
/*  text-align: center;*/
/*}*/
</style>