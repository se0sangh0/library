<?php
session_start();
include 'config.php'; // DB 연결을 위해 추가

// --- 관리자 인증 가드 ---
if (empty($_SESSION['is_admin']) || $_SESSION['is_admin'] !== true) {
    die("오류: 접근 권한이 없습니다.");
}

$c_program_path = 'C:/xampp/htdocs/library/library_project/library_cli/x64/Debug/library_cli.exe';

$action = $_POST['action'] ?? '';
$book_number_from_admin = $_POST['book_number'] ?? '';
$user_id = $_POST['user_id'] ?? '';
$command = '';

// --- 1. 유효성 검사 ---
if (empty($book_number_from_admin)) {
    die("도서 관리번호를 입력해야 합니다.");
}

// --- 2. 요청 작업에 따라 C 프로그램 명령어 생성 ---
if ($action === 'borrow') {
    // ...
    // C 프로그램에 book_number를 직접 전달하고, --book_id를 --book_number로 변경
    $command = 'chcp 65001 > NUL && ' . $c_program_path . 
               ' --borrow --user_id ' . escapeshellarg($user_id) . 
               ' --book_number ' . escapeshellarg($book_number_from_admin);

} elseif ($action === 'return') {
    $command = 'chcp 65001 > NUL && ' . $c_program_path . 
               ' --return --book_number ' . escapeshellarg($book_number_from_admin);
} 

// C 프로그램 실행 및 결과 캡처
$output = shell_exec($command . ' 2>&1');
$output = mb_convert_encoding($output, 'UTF-8', 'CP949');

?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>대출/반납 처리 결과</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .result-box {
            background-color: #333;
            color: #fff;
            padding: 20px;
            border-radius: 5px;
            font-family: 'Courier New', Courier, monospace;
            white-space: pre-wrap;
            text-align: left;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>대출/반납 처리 실행 결과</h1>
    <p><a href="admin.php">관리자 페이지로 돌아가기</a></p>
    <hr>

    <hr>
    <h3>C 프로그램 실행 결과:</h3>
    <div class="result-box">
        <?php echo htmlspecialchars($output); ?>
    </div>
</div>
</body>
</html>