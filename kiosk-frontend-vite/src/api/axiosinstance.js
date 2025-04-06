// src/api/axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create();

// 요청을 보내기 전에 토큰이 있다면 자동으로 헤더에 붙이기
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default axiosInstance;
