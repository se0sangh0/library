<?php
session_start(); // 세션을 사용하기 위해 시작

// 모든 세션 변수 지우기
session_unset();

// 세션 파괴
session_destroy();
//session_destroy() 함수는 서버에 저장된 로그인 정보를 완전히 삭제하여 사용자를 로그아웃 상태로 만듭니다.

echo "<script>alert('로그아웃 되었습니다.'); location.href='index.php';</script>";
exit();
?>