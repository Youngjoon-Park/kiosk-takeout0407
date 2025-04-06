// src/components/RequireAuth.jsx
import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';

function RequireAuth({ children }) {
  const [valid, setValid] = useState(null); // null = 확인 중

  useEffect(() => {
    setValid(true); // ✅ 무조건 인증 통과 (임시 테스트용)
  }, []);

  if (valid === null) return <div>⏳ 로딩 중...</div>;
  if (valid === false) return <Navigate to="/admin/login" replace />;
  return children;
}

export default RequireAuth;
