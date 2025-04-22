# 📸 PhotoRetouching

PhotoRetouching은 Android Jetpack Compose 기반의 사진 보정 앱입니다.  
사용자는 사진을 선택한 후 GPUImage 기반의 다양한 보정 필터를 적용하고 결과를 저장할 수 있습니다.

---

## 🔧 기술 스택

- **언어 및 프레임워크**
  - Kotlin
  - Jetpack Compose
  - MVVM + Clean Architecture

- **라이브러리 및 도구**
  - GPUImage for Android (CyberAgent)
  - Hilt (DI)
  - Coil (이미지 로딩)
  - Custom OpenGL Shaders
  - Coroutine + Flow

---

## 주요 기능

### 사진 선택
- 갤러리에서 사진을 선택하고 화면에 표시

### 이미지 보정
- **기본 보정 기능**
  - 밝기, 대비, 채도, 노출
- **고급 보정 기능 (Custom Filter로 직접 구현)**
  - 하이라이트 / 섀도우 조절
  - **Tint** (보라 ↔ 초록 계열)
  - **Clarity (명료도)** 조절
  - **Light Balance (라이트 밸런스)** – 밝은 부분/어두운 부분 강조
- GPUImage 기반으로 고성능 실시간 필터 처리

### 결과 저장
- JPG/PNG/WEBP 포맷으로 저장
- 사용자 선택 저장 옵션 제공

### 사용자 경험(UI/UX)
- LazyRow로 보정 옵션을 수평 스크롤로 선택
- Compose 기반의 커스텀 토스트 (`RetouchingToast`)
- 다크모드 대응
- 메인화면에서 종료 여부 확인 후 종료

---

## 📁 아키텍처 구조
├── di // Hilt 모듈 
├── domain // 비즈니스 로직 (UseCase, Repository Interface 등) 
├── data // 데이터 처리 (Repository 구현, DataSource) 
├── presentation // Compose UI 및 ViewModel 
├── gpu // GPUImage 관련 커스텀 필터(GLSL 포함) 
└── util // 공통 유틸 및 확장 함수

- ViewModel → `Map<RetouchingOption, Float>` 형태로 모든 보정 상태 통합 관리
- `StateFlow`로 Compose에 반응형 상태 전달

---

##스크린샷

(여기에 앱 UI 캡처 이미지 추가)
![darkTheme](https://github.com/user-attachments/assets/a208e6a1-41f7-4834-9ca9-0e5df85a72b2) ![lightTheme](https://github.com/user-attachments/assets/5034571e-10e2-4b1d-a042-83970d61cb15)

---

## 향후 개선 방향

- Undo/Redo 기능 추가
- 보정 적용 전/후 비교 기능
- iOS 또는 Desktop 대응을 위한 KMP 구조 고려
- 실시간 필터 성능 개선 (OpenGL 최적화)
