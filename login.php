<?php
// 이 페이지에 접속했을 때, 만약 이미 로그인 상태라면 메인 페이지로 보내버립니다.
session_start();
if (isset($_SESSION['username'])) {
    header('Location: index.php');
    exit();
}
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>
    <h2>도서관 로그인</h2>
    <form action="login_process.php" method="POST">
        <p>아이디: <input type="text" name="username" required></p>
        <p>비밀번호: <input type="password" name="password" required></p>
        <p><input type="submit" value="로그인"></p>
    </form>
    <hr>
    <p>
        아직 회원이 아니신가요? <a href="join.php">회원가입</a>
    </p>
    <p>
        아이디/비밀번호를 잊으셨나요? <a href="find_id_pw.php">아이디/비밀번호 찾기</a>
    </p>
</body>
</html>