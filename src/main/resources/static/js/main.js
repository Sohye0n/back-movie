// main.js (JavaScript 파일의 시작 부분)
import axios from 'axios';

// Axios 인터셉터 등록
axios.interceptors.request.use(
  function (config) {
    // 토큰을 로컬 스토리지나 쿠키에서 가져옵니다.
    const accessToken = localStorage.getItem('accessToken');

    // 토큰이 있다면 헤더에 추가합니다.
    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// 이후 웹 페이지에서 Axios를 사용하는 다른 JavaScript 코드들이 여기에 이어집니다.
