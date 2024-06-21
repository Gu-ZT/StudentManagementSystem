<script setup lang="ts">
import {Ref, ref} from "vue";
import {Request} from "../request";
import {goToHome} from "../router";
import {AESUtil, MD5Util, RSAUtil} from "../util";

const loginData: Ref<any> = ref({
  username: undefined,
  password: undefined
});

function login() {
  Request.get("/auth/public", (ctx: any) => {
    let serverKey = ctx.msg;
    localStorage.setItem("ServerPublicKey", serverKey);
    Request.post("/login/login", {
      username: loginData.value.username,
      password: RSAUtil.encrypt(MD5Util.encrypt(loginData.value.password), serverKey),
    }, ctx => {
      localStorage.setItem("id", ctx.data.id);
      localStorage.setItem("nickname", ctx.data.nickname);
      localStorage.setItem("token", ctx.data.token);
      localStorage.setItem("pwd", MD5Util.encrypt(AESUtil.encrypt(loginData.value.password, '666')));
      let key = RSAUtil.generateKey();
      localStorage.setItem("PublicKey", key.public);
      localStorage.setItem("PrivateKey", key.private);
      goToHome();
    });
  });
}
</script>

<template>
  <div class="container">
    <a-card class="card">
      <template #title>
        登录
      </template>
      <a-input v-model:value="loginData.username" class="input" placeholder="用户名"/>
      <a-input-password v-model:value="loginData.password" class="input" placeholder="密码"/>
      <a-button type="primary" size="large" @click="login">登录</a-button>
      <br/>
      <a href="/#/register">没有账号，前往注册</a>
    </a-card>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  height: 400px;
}

.input {
  margin: 5px;
}

.card {
  display: inline-block;
  width: 40%;
  margin: auto;
  text-align: center;
}
</style>