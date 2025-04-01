
import axios from "axios";
// 使用element-plus ElMessage做消息提醒
import { ElMessage } from 'element-plus'

//1. 创建新的axios实例，
const service = axios.create({
  // 公共接口 开发环境还是线上环境也可以用api
  baseURL: "/api",
  // 超时时间 单位是ms，这里设置了50s的超时时间
  timeout: 50000,
});
// 2.请求拦截器
service.interceptors.request.use(
  (config) => {
    //发请求前做的一些处理，数据转化，配置请求头，设置token,设置loading等，根据需求去添加
    const token = localStorage.getItem("token")
    if (token) {
      config.headers.token = token; //如果要求携带在请求头中
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

// 3.响应拦截器
service.interceptors.response.use(
  (response) => {
    //接收到响应数据并成功后的一些共有的处理，关闭loading等
    if (response && response.data) {
      const res = response.data;
      if (res.code != 200) {
        ElMessage({
          message: res.msg ? res.msg : "接口有异常",
          type: 'error',
        })
      }
      return response.data
    }
    return response;
  },
  (error) => {
    // 超时处理
    if (JSON.stringify(error).includes("timeout")) {
      ElMessage.error("服务器响应超时，请刷新当前页");
    }
    else if (error.message) {
      ElMessage.error(error.message);
    } else {
      ElMessage.error("服务器异常");
    }
    return Promise.resolve(error.response);
  }
);
//4.导出
export default service;

