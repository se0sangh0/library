<?php
session_start();
include 'config.php';

// 관리자만 접근 가능하도록 설정
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

// 폼에서 전송된 데이터 받기
$title = $_POST['title'];
$author = $_POST['author'];
$publisher = $_POST['publisher'];
$pubdate = $_POST['pubdate'];
$isbn = $_POST['isbn'];
$image = $_POST['image'];
$description = $_POST['description'];

// 1. 필수 값이 비어있는지 확인
if (empty($title) || empty($author) || empty($isbn)) {
    echo "<script>alert('필수 정보(제목, 저자, ISBN)가 누락되었습니다.'); history.back();</script>";
    exit();
}

// 2. ISBN으로 중복 등록 방지
$stmt = $conn->prepare("SELECT book_id FROM books WHERE isbn = ?");
$stmt->bind_param("s", $isbn);
$stmt->execute();
$result = $stmt->get_result();
if ($result->num_rows > 0) {
    echo "<script>alert('이미 등록된 도서입니다 (ISBN 중복).'); location.href='add_book.php';</script>";
    $stmt->close();
    $conn->close();
    exit();
}
$stmt->close();


// 3. DB에 새로운 도서 정보 삽입
// status는 'available'(대출 가능), loan_count는 0으로 기본 설정
$sql = "INSERT INTO books (title, author, publisher, pubdate, isbn, image, description, status, loan_count, added_date) VALUES (?, ?, ?, ?, ?, ?, ?, 'available', 0, NOW())";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sssssss", $title, $author, $publisher, $pubdate, $isbn, $image, $description);

if ($stmt->execute()) {
    echo "<script>alert('\"" . addslashes($title) . "\" 도서가 성공적으로 등록되었습니다.'); location.href='add_book.php';</script>";
} else {
    echo "<script>alert('도서 등록 중 오류가 발생했습니다: " . addslashes($stmt->error) . "'); history.back();</script>";
}

$stmt->close();
$conn->close();
?>