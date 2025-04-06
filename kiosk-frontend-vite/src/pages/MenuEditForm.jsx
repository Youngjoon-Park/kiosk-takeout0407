import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getMenuById, updateMenu } from "../api/adminMenuApi";

const MenuEditForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [menu, setMenu] = useState({
    name: "",
    price: 0,
    description: "",
  });

  useEffect(() => {
    const fetchMenu = async () => {
      try {
        const data = await getMenuById(id);
        setMenu({
          name: data.name,
          price: data.price,
          description: data.description,
        });
      } catch (error) {
        console.error("메뉴 정보 불러오기 실패:", error);
        alert("메뉴 정보를 불러오지 못했습니다.");
      }
    };
    fetchMenu();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMenu((prev) => ({
      ...prev,
      [name]: name === "price" ? parseInt(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateMenu(id, menu);
      alert("✅ 수정 완료!");
      navigate("/menu-test");
    } catch (error) {
      console.error("수정 실패:", error);
      alert("❌ 수정 실패!");
    }
  };

  return (
    <div>
      <h2>✏️ 메뉴 수정</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>이름: </label>
          <input name="name" value={menu.name} onChange={handleChange} />
        </div>
        <div>
          <label>가격: </label>
          <input
            name="price"
            type="number"
            value={menu.price}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>설명: </label>
          <input
            name="description"
            value={menu.description}
            onChange={handleChange}
          />
        </div>
        <button type="submit">수정</button>
      </form>
    </div>
  );
};

export default MenuEditForm;
