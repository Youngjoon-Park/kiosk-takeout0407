import React, { useState } from "react";
import { addMenu } from "../api/adminMenuApi"; 
import { useNavigate } from "react-router-dom";


const MenuAddForm = () => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [description, setDescription] = useState("");

  const navigate = useNavigate(); // 선언



  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addMenu({
        name,
        price: parseInt(price),
        description,
      });
      alert("메뉴가 등록되었습니다!");
      navigate("/menu-test"); // ✅ 메뉴 목록으로 이동
    } catch (error) {
      console.error("메뉴 추가 실패:", error);
      alert("등록 실패!");
    }
  };
  
  return (
    <div>
      <h2>🍽️ 메뉴 추가</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>이름: </label>
          <input value={name} onChange={(e) => setName(e.target.value)} />
        </div>
        <div>
          <label>가격: </label>
          <input
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />
        </div>
        <div>
          <label>설명: </label>
          <input
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <button type="submit">등록</button>
      </form>
    </div>
  );
};

export default MenuAddForm;
