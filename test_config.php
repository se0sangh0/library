<?php
ini_set('display_errors', 1);
error_reporting(E_ALL);

echo "config.php 파일을 포함(include)하려고 시도합니다...<br>";

include 'config.php';

echo "config.php 파일이 성공적으로 포함되었습니다.<br>";

if (isset($conn) && $conn->ping()) {
    echo "데이터베이스 연결($conn->host_info)이 활성화되어 있습니다.";
} else {
    echo "데이터베이스 연결 객체(\$conn)를 찾을 수 없거나 응답이 없습니다.";
}
?>