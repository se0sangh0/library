<?php
// 1. 세션 시작: 로그인 정보를 서버에 기록하기 위해 가장 먼저 호출합니다.
session_start();

include 'config.php'; // DB 연결

// 2. 사용자가 입력한 아이디와 비밀번호 받기
$username = $_POST['username'];
$password = $_POST['password'];

// 3. 아이디를 기준으로 사용자 정보 조회 (보안을 위해 PreparedStatement 사용)
$stmt = $conn->prepare("SELECT * FROM users WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

// 4. 조회된 사용자가 있는지 확인
if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();

    // 5. 비밀번호 검증 (DB에 저장된 해시값과 사용자가 입력한 비밀번호를 비교)
    if (password_verify($password, $user['password'])) {
        // 6. 비밀번호 일치: 로그인 성공 처리
        // 세션에 사용자 정보 저장 (이제 다른 페이지에서 이 정보를 쓸 수 있습니다)
        $_SESSION['user_id'] = $user['user_id'];
        $_SESSION['username'] = $user['username'];
        $_SESSION['name'] = $user['name'];
        $_SESSION['is_admin'] = (bool)$user['is_admin']; 

        // 로그인 성공 후 메인 페이지로 이동
        header('Location: index.php');
        exit();
    }
}

// 7. 로그인 실패: 아이디가 없거나, 비밀번호가 틀린 경우
echo "<script>alert('아이디 또는 비밀번호가 올바르지 않습니다.'); history.back();</script>";

$stmt->close();
$conn->close();
?>