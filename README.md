# 🛒 Kiosk Status System - 0406 버전

Java + Spring Boot + React + Vite 기반의 **실시간 주문 상태 관리 키오스크 시스템**입니다.  
관리자는 주문 내역을 확인하고 상태를 변경하며, WebSocket 기반으로 실시간 알림까지 받아볼 수 있도록 구성되어 있습니다.

> ✨ 본 프로젝트는 과거 `kiosk-app`, `kiosk-frontend` 등 분리된 프로젝트를 통합하고, 여러 실험과 시행착오를 거쳐 구축된 **현장형 통합 프로젝트**입니다.

---

## 📦 프로젝트 구조

```
kiosk-system-vite/
│
├─ kiosk-app/                # 백엔드(Spring Boot, REST API, WebSocket)
├─ kiosk-frontend-vite/      # 프론트엔드(React + Vite)
└─ README.md                 # 이 문서
```

---

## 🚀 핵심 기능

### ✅ 사용자 기능
- 메뉴 목록 조회
- 장바구니 담기
- 주문 요청
- 카카오페이 결제 연동 (QR 생성 → 결제 승인)

### ✅ 관리자 기능
- 주문 목록 확인
- 주문 상세 보기
- 주문 상태 변경(PENDING → COMPLETED)
- WebSocket으로 실시간 새 주문 수신

---

## ⚙️ 기술 스택

| 분류 | 기술 |
|------|------|
| 프론트엔드 | React 19, Vite 6, Axios |
| 백엔드 | Java 17, Spring Boot 3, Spring Web, JPA |
| 인증 | Spring Security + JWT |
| 실시간 처리 | WebSocket (STOMP + SockJS) |
| DB | MySQL |
| 빌드도구 | Gradle |
| 스타일링 | ❗ **Tailwind CSS 적용 예정** |
| 효과음 | ❗ **띠링 알림음 적용 예정** |

---

## 📌 주문 상태 저장 기능

### 상태 값 종류 (`status` 필드)
- PENDING: 주문 접수 대기 중
- COMPLETED: 관리자에 의해 완료 처리됨

> 확장 예정: PREPARING, COOKED, CANCELLED 등 단계별 추가 가능

### 주문 상태 저장 흐름

1. 사용자가 주문 요청 (POST /api/orders) → 기본 상태: `PENDING`
2. 관리자가 상태 변경 버튼 클릭 → PATCH /api/admin/orders/{id}/status
3. 서버에서 상태 변경 처리 및 응답 반환
4. WebSocket을 통해 실시간 알림 전송 (/topic/orders)

---

## 🧠 과거 개발 히스토리 & 시행착오

### ❗ OrderStatus Enum 처리 실패 → 문자열 전환

#### 원래 설계
```java
public enum OrderStatus {
    PENDING, COMPLETED
}
```

- 프론트에서 `{ "status": "COMPLETED" }` 전송 시
- `@RequestBody OrderStatus status` 매핑에서 400 Bad Request 오류 발생

#### 시도한 해결책
- `@JsonCreator`, `@JsonValue` 적용 → 실패
- DTO로 감싸기 → 실패
- valueOf 수동 매핑 시도

#### 최종 해결
✅ **Enum 자체를 삭제하고 `String status`로 단순화**
```java
private String status;  // Enum 대신 문자열로 상태 처리
```

#### 이유와 교훈
- Enum은 직렬화 오류와 관리 이슈가 많았음
- 문자열 기반 구조가 프론트와 연동 및 확장성 측면에서 훨씬 유리함
- 유효값 검증은 수동으로 처리 가능하며 실무에 더 적합함

---

## ⚙️ 배포 히스토리

### ✅ 일반 `.jar` 파일 수동 배포
- `./gradlew bootJar`로 빌드하여 생성된 `kiosk-app-0.0.1-SNAPSHOT.jar` 파일을
- 실제 키오스크 장비에 수동 복사 후 실행
```bash
java -jar kiosk-app-0.0.1-SNAPSHOT.jar
```

- 정적 리소스 포함 (React build 결과물을 `/static`에 내장)

#### 문제점
- 파일 용량이 60MB 이상 → GitHub 업로드 시 경고 발생
- 키오스크 PC에서는 실행되나 자동 실행, 백그라운드 처리 미지원

---

### ❌ Docker 배포 실패 회고
- Dockerfile 구성 후 컨테이너 빌드 성공
```dockerfile
FROM openjdk:17
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

- `docker run -p 8081:8081` 으로 실행했으나
  - 외부 접속 불가 (localhost만 바인딩)
  - 환경 변수, 포트 설정 누락

#### 교훈
- Docker는 배포 자동화에 강력하지만 **환경 세팅이 까다로움**
- 초기에 `.jar` 수동 배포가 더 빠르고 안정적일 수 있음

---

## 🌐 포트 구성

| 서비스 | 포트 |
|--------|------|
| 프론트엔드 (Vite) | 5173 |
| 백엔드 (Spring Boot) | 8081 |

- `application.properties`에서 명시적으로 지정
```properties
server.port=8081
```
- 프론트에서는 Axios 또는 `.env` 파일로 API 서버 주소 지정

---

## 🧪 실행 방법

### 📌 백엔드 실행
```bash
cd kiosk-app
./gradlew bootRun
```

### 📌 프론트엔드 실행
```bash
cd kiosk-frontend-vite
npm install
npm run dev
```

접속 URL: [http://localhost:5173/admin/orders](http://localhost:5173/admin/orders)

---

## 📢 실시간 알림 (WebSocket)

- WebSocket 채널 `/topic/orders` 구독
- 주문 발생 시 관리자 페이지에서 실시간 반영
- 추후 **소리 알림(ding.mp3)** 기능 추가 예정

---

## ✅ 향후 계획 (To-Do)

- [ ] 주문 상태를 단계별로 세분화 (PREPARING, DONE 등)
- [ ] Tailwind CSS 스타일링 적용
- [ ] WebSocket 알림 시 효과음(`띠링`) 재생
- [ ] 바코드/스캐너 연동
- [ ] Docker 정식 배포 재시도
- [ ] 키오스크 장비 자동 실행 설정

---

README-업데이트-0407.md (최종 교육/실습 버전)

# ✅ kiosk-takeout0407 – 2025.04.07 작업 정리 (실제 반영된 사항 위주)

---

## 1️⃣ [기능 개선] 포장/매장 구분 (`takeOut`) WebSocket 및 키친 화면 연동

- 주문 생성 시 `takeOut = true/false` 값 포함
- WebSocket 메시지에 포함되도록 DTO 수정
- 키친 화면에서 매장/포장 구분 아이콘으로 렌더링됨
- `takeOut` 누락 시 키친에서 주문이 안 뜨는 문제 발생 → DTO 필드 추가로 해결

---

## 2️⃣ [이슈 해결] WebSocket 메시지 누락 문제 (두 번째 주문부터 안 나옴)

- DTO(`OrderDetailResponse`)에 `takeOut`, `paid` 필드 누락으로 메시지 불완전
- WebSocket 전송 시 DTO 기준으로 메시지 구성
- 키친 화면에서 정상 출력됨

---

## 3️⃣ [이슈 해결] Tailwind CSS 적용 오류 → 버전 다운그레이드로 해결

- 자동 설정 실패 (`tailwind.config.js`, `postcss.config.js` 생성 안됨)
- 다음 명령어로 버전 수동 지정 및 설치:

npm install tailwindcss@3.3.5 postcss@8.4.21 autoprefixer@10.4.13
설정 파일 수동 생성 후 Tailwind CSS 정상 적용됨

4️⃣ [테스트] NFC 결제 흐름 테스트용 orderId = 1 데이터 사용
NFC 결제 흐름을 점검하기 위해 테스트용 주문을 임의로 생성

프론트에서 orderId = 1로 가정하고 NFC 결제 완료 API 호출

백엔드 API:

http

POST /api/payments/nfc
Body:
{
  "orderId": 1,
  "amount": 9500
}
결과: paid = true, paidAt 저장, 키친 화면 WebSocket 전송 완료됨


항목	설명
DTO 누락 → 해결	takeOut, paid 필드 추가
WebSocket 반영	DTO 기준 메시지로 변경 후 정상 출력
Tailwind 오류 해결	수동 설정 + 버전 다운그레이드
테스트 데이터	orderId = 1로 NFC 결제 흐름 검증
교육 구성	버튼 클릭 방식 vs Postman 방식 비교 설명-

### ✅ 저장 및 커밋 방법

```bash
git add README-업데이트-0407.md
git commit -m "추가: 테스트 orderId=1, 교육용 NFC 버튼 + Postman 비교 포함"
git push


---

> "실무에서 겪은 시행착오와 실전형 요구사항을 바탕으로, 빠르게 현장에 도입할 수 있는 키오스크 시스템을 목표로 구축했습니다."
