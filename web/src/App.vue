@ -1,68 +1,98 @@
<script setup lang="ts">
import Icon from './assets/icon.svg';
import {message} from 'ant-design-vue';
import {Operate} from "./util";

const [messageApi, contextHolder] = message.useMessage();
Operate.messageApi = messageApi;
</script>

<template>
  <context-holder/>
  <a-page-header
      class="app-header"
      title="学生信息管理系统"
      sub-title="一个非常好使的学生信息管理系统"
      :avatar="{ src: Icon }">
  </a-page-header>
  <a-layout>
    <a-layout-content>
      <div class="app-scrollbar">
        <div class="app-content">
          <router-view/>
        </div>
        <div class="app-footer">
          <span>
            ©Gugle
          </span>
        </div>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<style scoped lang="scss">
.app-header {
  border: 1px solid rgb(235, 237, 240);
  background-color: #ffffff;
}

.app-sider {
  height: calc(100vh - 82px);
}

.app-menu {
  height: 100%;
  max-width: 280px;
}

.app-scrollbar {
  height: calc(100vh - 82px);
  overflow: auto;
  scrollbar-gutter: stable;
}

.app-footer {
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.app-content {
  margin: 5px;
  min-height: calc(100vh - 173px);
}
</style>
