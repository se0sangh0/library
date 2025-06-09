<?php
session_start();
include 'config.php';

// --- 인증 가드 ---
if (!isset($_SESSION['user_id'])) {
    echo "<script>alert('로그인이 필요합니다.'); location.href='login.php';</script>";
    exit();
}

// --- 데이터 수신 ---
$user_id = $_SESSION['user_id'];
$title = $_POST['title'];
$author = $_POST['author'];
$publisher = $_POST['publisher'];
$pubdate = $_POST['pubdate'];
$isbn = $_POST['isbn'];
$image = $_POST['image'];
$description = $_POST['description'];

// --- 유효성 검사 ---
if (empty($title)) {
    echo "<script>alert('도서 제목이 없습니다.'); history.back();</script>";
    exit();
}

// --- 로직 처리 ---

// 1. 우리 DB에 이미 소장된 책인지 ISBN으로 확인
$stmt = $conn->prepare("SELECT book_id FROM books WHERE isbn = ?");
$stmt->bind_param("s", $isbn);
$stmt->execute();
if ($stmt->get_result()->num_rows > 0) {
    echo "<script>alert('이미 소장 중인 도서입니다.'); history.back();</script>";
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();

// 2. 이미 내가 'pending' 상태로 신청한 책인지 확인
$stmt = $conn->prepare("SELECT request_id FROM book_requests WHERE isbn = ? AND user_id = ? AND status = 'pending'");
$stmt->bind_param("si", $isbn, $user_id);
$stmt->execute();
if ($stmt->get_result()->num_rows > 0) {
    echo "<script>alert('이미 신청하신 도서입니다. 관리자의 승인을 기다려주세요.'); history.back();</script>";
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();

// 3. book_requests 테이블에 새로운 신청 정보 INSERT
// status의 기본값은 'pending'이므로 따로 명시할 필요 없음
$sql = "INSERT INTO book_requests (user_id, book_title, book_author, isbn, publisher, pubdate, image, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);
$stmt->bind_param("isssssss", $user_id, $title, $author, $isbn, $publisher, $pubdate, $image, $description);

if ($stmt->execute()) {
    echo "<script>alert('도서 신청이 완료되었습니다. 관리자 승인 후 등록됩니다.'); location.href='search.php';</script>";
} else {
    echo "<script>alert('신청 처리 중 오류가 발생했습니다.'); history.back();</script>";
}

$stmt->close();
$conn->close();
?>