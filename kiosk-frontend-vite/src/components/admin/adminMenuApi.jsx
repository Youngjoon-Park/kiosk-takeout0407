// 📁 src/api/adminMenuApi.jsx

import axios from "axios";

// ✅ 사용자용 공개 API로 수정
export const getAllMenus = async () => {
  const response = await axios.get("http://localhost:8081/api/menus");
  return response.data;
};


// ✅ 관리자용 메뉴 삭제
export const deleteMenu = async (id) => {
  return await axios.delete(`http://localhost:8080/api/admin/menus/${id}`);
};
