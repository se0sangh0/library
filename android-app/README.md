# LibraryApp Android 클라이언트

기존 PHP + C 기반 도서관 관리 예제를 안드로이드 스튜디오에서 실행할 수 있는 네이티브 애플리케이션 구조로 변환했습니다. `android-app` 디렉터리를 Android Studio에서 열면 바로 실행할 수 있습니다.

## 주요 기능
- 이메일/비밀번호 기반 로그인 및 회원가입 흐름
- 대출/예약 현황을 한눈에 확인할 수 있는 홈 화면과 탭 UI
- 도서 검색 및 상세 하단 시트, 대출/예약 액션
- 내 정보 화면과 관리자 승인 요청 리스트
- 메모리 기반 저장소(`InMemoryLibraryRepository`)로 테스트 데이터 제공

## 실행 방법
1. Android Studio에서 **Open** 을 선택하고 `android-app` 디렉터리를 열어주세요.
2. Gradle Sync 후 에뮬레이터 또는 실제 기기를 선택해 실행합니다.
3. 기본 계정
   - 관리자: `admin@library.com`
   - 일반 사용자: `user@library.com`
   - 비밀번호는 아무 문자열이나 입력하면 됩니다(데모용 인증 로직).

필요에 따라 `InMemoryLibraryRepository`를 실제 API 호출 로직으로 교체하면 됩니다.
