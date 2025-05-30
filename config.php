<?php
$host = "localhost";
$db_user = "root"; // XAMPP 기본값
$db_pass = ""; // XAMPP 기본값 (비밀번호를 설정했다면 변경 필요)
$db_name = "library_db";

$conn = new mysqli($host, $db_user, $db_pass, $db_name);

if ($conn->connect_error) {
    die("DB 연결 실패: " . $conn->connect_error);
}
// DB 통신 시 문자셋을 utf8로 설정 (한글 깨짐 방지)
$conn->set_charset("utf8");
?>