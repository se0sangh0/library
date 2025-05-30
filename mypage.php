<?php
session_start();

// 로그인 상태가 아니라면, 로그인 페이지로 쫓아낸다.
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

// --- 이 아래부터는 로그인한 사용자만 볼 수 있는 페이지 내용 ---
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
</head>
<body>
    <h1>마이페이지</h1>
    <p><?php echo htmlspecialchars($_SESSION['name']); ?>님의 회원 정보 및 대출 내역입니다.</p>
    <p><a href="index.php">메인으로 돌아가기</a></p>
</body>
</html>