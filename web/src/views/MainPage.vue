<script setup lang="ts">
import dayjs from 'dayjs';
import {goToHome, goToLogin} from "../router";
import {Ref, ref} from "vue";
import {AESUtil, dataCopy, MD5Util, RSAUtil} from "../util";
import {Request} from "../request";
import {UserOutlined, PlusOutlined, LoadingOutlined} from "@ant-design/icons-vue";
import {message, UploadChangeParam} from "ant-design-vue";

let token = localStorage.getItem("token");
if (token == null) goToLogin();
let nick = localStorage.getItem("nickname");
const data: Ref<any> = ref([]);

const columns = [
  {
    title: '近照',
    dataIndex: 'image',
    key: 'image',
  },
  {
    title: '学号',
    dataIndex: 'studentId',
    key: 'studentId',
    defaultSortOrder: 'descend',
    sorter: (a: any, b: any) => a.studentId - b.studentId
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '性别',
    dataIndex: 'sex',
    key: 'sex',
  },
  {
    title: '生日',
    dataIndex: 'birth',
    key: 'birth',
  },
  {
    title: 'QQ',
    dataIndex: 'qq',
    key: 'qq',
  },
  {
    title: '电话',
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: '地址',
    dataIndex: 'address',
    key: 'address',
  },
  {
    title: '操作',
    dataIndex: 'function',
    key: 'function',
  }
];
getData();

function getData() {
  Request.get("/student/get", ctx => {
    data.value = ctx.data;
    for (let elem of data.value) {
      elem.birth = elem.birth.toString().slice(0, 10);
    }
  });
}

const fileList = ref([]);
const loading = ref<boolean>(false);
const imageUrl = ref<string>('');

function handleChange(info: UploadChangeParam) {
  if (info.file.status === 'uploading') {
    loading.value = true;
    return;
  }
  if (info.file.status === 'done') {
    imageUrl.value = JSON.parse(info.file.xhr.response).msg;
  }
  if (info.file.status === 'error') {
    loading.value = false;
    message.error('upload error');
  }
}

function beforeUpload(file: any) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('You can only upload JPG or PNG file!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('Image must smaller than 2MB!');
  }
  return isJpgOrPng && isLt2M;
}

const addData: Ref<any> = ref({
  studentId: undefined,
  name: undefined,
  sex: undefined,
  birth: undefined,
  qq: undefined,
  phone: undefined,
  address: undefined,
  image: undefined,
});
const addOpen = ref(false);

function openAdd() {
  imageUrl.value = '';
  addData.value = {
    studentId: undefined,
    name: undefined,
    sex: undefined,
    birth: undefined,
    qq: undefined,
    phone: undefined,
    address: undefined,
    image: undefined,
  }
  addOpen.value = true;
}

function add() {
  if (imageUrl.value) addData.value.image = imageUrl.value;
  Request.post("/student/add", addData.value, _ => {
    addData.value = {
      studentId: undefined,
      name: undefined,
      sex: undefined,
      birth: undefined,
      qq: undefined,
      phone: undefined,
      address: undefined,
      image: undefined,
    }
    addOpen.value = false;
    getData();
  });
}

const changeData: Ref<any> = ref({
  id: undefined,
  studentId: undefined,
  name: undefined,
  sex: undefined,
  birth: undefined,
  qq: undefined,
  phone: undefined,
  address: undefined,
  image: undefined,
});
const changeOpen = ref(false);

function openChange(data: any) {
  imageUrl.value = data.image;
  changeData.value = dataCopy(data);
  changeData.value.birth = dayjs(changeData.value.birth);
  changeOpen.value = true;
}

function change() {
  if (imageUrl.value) changeData.value.image = imageUrl.value;
  Request.put("/student/change", changeData.value, _ => {
    changeData.value = {
      id: undefined,
      studentId: undefined,
      name: undefined,
      sex: undefined,
      birth: undefined,
      qq: undefined,
      phone: undefined,
      address: undefined,
      image: 0,
    }
    changeOpen.value = false;
    getData();
  });
}

function remove(id: any) {
  Request.del(`/student/del/${id}`, _ => {
    getData();
  });
}

Request.post("/auth/test/get", {
  key: localStorage.getItem("PublicKey")
}, (ctx: any) => {
  let key = localStorage.getItem("PrivateKey");
  if (key) console.log(RSAUtil.decrypt(ctx.msg, key));
})

function getImage(id: string) {
  return `${Request.baseURL}/file/get/${id}`;
}

const changePwdData: Ref<any> = ref({
  old: undefined,
  password: undefined,
  rePassword: undefined,
});
const changePwdOpen = ref(false);

function openChangePwd() {
  changePwdData.value = {
    old: undefined,
    password: undefined,
    rePassword: undefined,
  }
  changePwdOpen.value = true;
}

function changePwd() {
  if (changePwdData.value.password !== changePwdData.value.rePassword) return;
  Request.get("/auth/public", (ctx: any) => {
    let serverKey = ctx.msg;
    localStorage.setItem("ServerPublicKey", serverKey);
    Request.put("/login/pwd", {
      old: RSAUtil.encrypt(MD5Util.encrypt(changePwdData.value.old), serverKey),
      password: RSAUtil.encrypt(MD5Util.encrypt(changePwdData.value.password), serverKey),
    }, _ => {
      changePwdData.value = {
        old: undefined,
        password: undefined,
        rePassword: undefined,
      }
      changePwdOpen.value = false;
    });
  });
}
</script>

<template>
  <a-space>
    你好，{{ nick }}
    <a-button @click="openChangePwd" type="primary">修改密码</a-button>
    <a-button @click="goToLogin" type="primary" danger>退出登录</a-button>
    <a-button @click="openAdd" type="primary">新增</a-button>
  </a-space>
  <a-table :dataSource="data" :columns="columns">
    <template #bodyCell="{ column,record }">
      <template v-if="column.key === 'image'">
        <a-avatar :size="64">
          <template #icon>
            <UserOutlined v-if="record.image == 0"/>
            <a-image v-else :src="getImage(record.image)"/>
          </template>
        </a-avatar>
      </template>
      <template v-if="column.key === 'function'">
        <a-button @click="openChange(record)" type="primary">修改</a-button>
        <a-popconfirm
            title="你确定要删除吗？"
            ok-text="Yes"
            cancel-text="No"
            @confirm="remove(record.id)"
        >
          <a-button type="primary" danger>删除</a-button>
        </a-popconfirm>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="addOpen" title="添加" @ok="add">
    <a-upload
        v-model:file-list="fileList"
        name="avatar"
        list-type="picture-card"
        class="avatar-uploader"
        :show-upload-list="false"
        :action="`${Request.baseURL}/file/upload`"
        :before-upload="beforeUpload"
        @change="handleChange"
    >
      <a-avatar v-if="imageUrl" :src="getImage(imageUrl)" size="large"/>
      <div v-else>
        <loading-outlined v-if="loading"></loading-outlined>
        <plus-outlined v-else></plus-outlined>
        <div class="ant-upload-text">Upload</div>
      </div>
    </a-upload>
    <a-input v-model:value="addData.studentId" placeholder="学号"/>
    <a-input v-model:value="addData.name" placeholder="姓名"/>
    <a-input v-model:value="addData.sex" placeholder="性别"/>
    <a-date-picker v-model:value="addData.birth" placeholder="生日" style="width: 100%"/>
    <a-input v-model:value="addData.qq" placeholder="QQ"/>
    <a-input v-model:value="addData.phone" placeholder="电话"/>
    <a-input v-model:value="addData.address" placeholder="地址"/>
  </a-modal>
  <a-modal v-model:open="changeOpen" title="修改" @ok="change">
    <a-upload
        v-model:file-list="fileList"
        name="avatar"
        list-type="picture-card"
        class="avatar-uploader"
        :show-upload-list="false"
        :action="`${Request.baseURL}/file/upload`"
        :before-upload="beforeUpload"
        @change="handleChange"
    >
      <a-avatar v-if="imageUrl" :src="getImage(imageUrl)" size="large"/>
      <div v-else>
        <loading-outlined v-if="loading"></loading-outlined>
        <plus-outlined v-else></plus-outlined>
        <div class="ant-upload-text">Upload</div>
      </div>
    </a-upload>
    <a-input v-model:value="changeData.studentId" placeholder="学号"/>
    <a-input v-model:value="changeData.name" placeholder="姓名"/>
    <a-input v-model:value="changeData.sex" placeholder="性别"/>
    <a-date-picker v-model:value="changeData.birth" placeholder="生日" style="width: 100%"/>
    <a-input v-model:value="changeData.qq" placeholder="QQ"/>
    <a-input v-model:value="changeData.phone" placeholder="电话"/>
    <a-input v-model:value="changeData.address" placeholder="地址"/>
  </a-modal>

  <a-modal v-model:open="changePwdOpen" title="修改" @ok="changePwd">
    <a-input-password v-model:value="changePwdData.old" placeholder="旧密码"/>
    <a-input-password v-model:value="changePwdData.password" placeholder="新密码"/>
    <a-input-password v-model:value="changePwdData.rePassword" placeholder="重复密码"/>
    <a-alert v-if="changePwdData.password !== changePwdData.rePassword" type="error" message="两次输入的密码不一致"/>
  </a-modal>
</template>

<style scoped>

</style>