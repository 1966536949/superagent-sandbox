<template>
  <div class="home-progress">
    <div class="title">月访问量</div>
    <div class="tip">同上期增长</div>
    <el-progress
        :percentage="20"
        :stroke-width="24"
        :text-inside="true"
        class="progress"
        status="success"
    ></el-progress>
    <div class="title">月用户量</div>
    <div class="tip">同上期增长</div>
    <el-progress
        :percentage="50"
        :stroke-width="24"
        :text-inside="true"
        class="progress"
        status="success"
    ></el-progress>
    <div class="title">月收入</div>
    <div class="tip">同上期减少</div>
    <el-progress
        :percentage="28"
        :stroke-width="24"
        :text-inside="true"
        class="progress"
        status="exception"
    ></el-progress>
  </div>
</template>
<script>
export default {};
</script>
<style lang="scss">
.home-progress {
  background: #ffffff;
  height: 400px;
  padding: 20px;

  .title {
    color: #666666;
    font-weight: bold;
    font-size: 20px;
    margin-top: 10px;
  }

  .tip {
    color: #888888;
    font-size: 16px;
    margin-top: 10px;
  }

  .progress {
    margin-top: 10px;
  }
}
</style>
