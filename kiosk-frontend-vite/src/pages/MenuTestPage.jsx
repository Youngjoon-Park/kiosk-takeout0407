import React, { useEffect, useState } from "react";
import { getAllMenus, deleteMenu } from "../api/adminMenuApi";
import { Link } from "react-router-dom";

const MenuTestPage = () => {
  const [menus, setMenus] = useState([]);

  // ✅ 메뉴 불러오기 함수 분리
  const fetchMenus = async () => {
    try {
      const data = await getAllMenus();
      setMenus(data);
    } catch (error) {
      console.error("메뉴 불러오기 실패:", error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      try {
        await deleteMenu(id);
        alert("삭제되었습니다!");
        setMenus(prev => prev.filter(menu => menu.id !== id)); // ✅ 목록에서 직접 제거
      } catch (error) {
        alert("삭제 실패!");
      }
    }
  };
  

  useEffect(() => {
    fetchMenus();
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h2>📋 등록된 메뉴 목록</h2>
      <ul style={{ listStyle: "none", padding: 0 }}>
        {menus.map((menu) => (
          <li key={menu.id} style={{ marginBottom: "12px" }}>
            <strong>{menu.name}</strong> - {menu.price.toLocaleString()}원
            <Link to={`/menu-edit/${menu.id}`}>
              <button style={{ marginLeft: "10px" }}>수정</button>
            </Link>
            <button onClick={() => handleDelete(menu.id)} style={{ marginLeft: "6px" }}>
              삭제
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MenuTestPage;
