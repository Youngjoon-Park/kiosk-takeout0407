// 📁 src/api/adminMenuApi.jsx
import axios from "axios";

// ✅ 사용자용 공개 API
// ✅ 불필요한 공백 제거! 절대 줄바꿈 없게
export const getAllMenus = async () => {
  const response = await axios.get("http://localhost:8081/api/menus");
  return response.data;
};


// ✅ 관리자용 메뉴 추가
export const addMenu = async (menu) => {
  const token = localStorage.getItem("token");  // 로컬스토리지에서 토큰을 가져옵니다.
  const response = await axios.post("http://localhost:8081/api/admin/menus", menu, {
    headers: {
      Authorization: `Bearer ${token}`  // 헤더에 JWT 토큰을 추가합니다.
    }
  });
  return response.data;
};

// ✅ 관리자용 메뉴 삭제
export const deleteMenu = async (id) => {
  const token = localStorage.getItem("token");  // 로컬스토리지에서 토큰을 가져옵니다.
  await axios.delete(`http://localhost:8081/api/admin/menus/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`  // 헤더에 JWT 토큰을 추가합니다.
    }
  });
};

// ✅ 관리자용 메뉴 조회 (ID로)
export const getMenuById = async (id) => {
  const token = localStorage.getItem("token");  // 로컬스토리지에서 토큰을 가져옵니다.
  const response = await axios.get(`http://localhost:8081/api/admin/menus/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`  // 헤더에 JWT 토큰을 추가합니다.
    }
  });
  return response.data;
};

// ✅ 관리자용 메뉴 수정
export const updateMenu = async (id, menu) => {
  const token = localStorage.getItem("token");  // 로컬스토리지에서 토큰을 가져옵니다.
  await axios.put(`http://localhost:8081/api/admin/menus/${id}`, menu, {
    headers: {
      Authorization: `Bearer ${token}`  // 헤더에 JWT 토큰을 추가합니다.
    }
  });
};
