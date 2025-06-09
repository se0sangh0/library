<?php
session_start();
include 'config.php';

// 1. 로그인 상태 확인 (비로그인 사용자는 접근 불가)
if (!isset($_SESSION['user_id'])) {
    echo "<script>alert('로그인이 필요합니다.'); location.href='login.php';</script>";
    exit();
}

// 2. 예약할 책의 ID와 사용자 ID를 변수에 저장
$book_id = $_POST['book_id'];
$user_id = $_SESSION['user_id'];

if (empty($book_id)) {
    echo "<script>alert('잘못된 접근입니다.'); history.back();</script>";
    exit();
}

// 3. 중복 예약 방지: 이미 이 책을 예약했는지 확인
$stmt = $conn->prepare("SELECT reservation_id FROM reservations WHERE book_id = ? AND user_id = ? AND status = 'active'");
$stmt->bind_param("ii", $book_id, $user_id);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    echo "<script>alert('이미 예약하신 도서입니다.'); history.back();</script>";
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();

// 4. 자신이 대출 중인 책은 예약할 수 없도록 방지
$stmt = $conn->prepare("SELECT loan_id FROM loans WHERE book_id = ? AND user_id = ? AND return_date IS NULL");
$stmt->bind_param("ii", $book_id, $user_id);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    echo "<script>alert('현재 대출 중인 도서는 예약할 수 없습니다.'); history.back();</script>";
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();


// 5. reservations 테이블에 예약 정보 삽입
$stmt = $conn->prepare("INSERT INTO reservations (book_id, user_id) VALUES (?, ?)");
$stmt->bind_param("ii", $book_id, $user_id);

if ($stmt->execute()) {
    echo "<script>alert('도서 예약이 완료되었습니다.'); history.back();</script>";
} else {
    echo "<script>alert('예약 처리 중 오류가 발생했습니다.'); history.back();</script>";
}

$stmt->close();
$conn->close();
?>