<?php
include 'config.php'; // DB 연결

// POST로 받은 데이터 변수에 저장
$name = $_POST['name'];
$phone = $_POST['phone'];
$birthdate = $_POST['birthdate'];
$email = $_POST['email'];
$username = $_POST['username'];
$password = $_POST['password'];
$password_confirm = $_POST['password_confirm'];

// 1. 필수 값 확인
if (empty($name) || empty($phone) || empty($birthdate) || empty($username) || empty($password)) {
    die("<script>alert('필수 항목을 모두 입력해주세요.'); history.back();</script>");
}

// 2. 비밀번호와 비밀번호 확인이 일치하는지 검사
if ($password !== $password_confirm) {
    die("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
}

// 3. 이름과 생년월일이 동일한 사용자가 있는지 확인 (요구사항)
$stmt = $conn->prepare("SELECT * FROM users WHERE name = ? AND birthdate = ?");
$stmt->bind_param("ss", $name, $birthdate);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    $stmt->close();
    die("<script>alert('이미 가입된 회원입니다. 로그인 해주세요.'); location.href='login.php';</script>");
}
$stmt->close();


// 4. 비밀번호 암호화 (보안상 매우 중요!)
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

// 5. DB에 회원 정보 저장 (SQL Injection 방지를 위해 PreparedStatement 사용)
$stmt = $conn->prepare("INSERT INTO users (username, password, name, phone, birthdate, email) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $username, $hashed_password, $name, $phone, $birthdate, $email);

if ($stmt->execute()) {
    echo "<script>alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.'); location.href='login.php';</script>";
} else {
    echo "<script>alert('회원가입 중 오류가 발생했습니다: " . $stmt->error . "'); history.back();</script>";
}

$stmt->close();
$conn->close();
?>