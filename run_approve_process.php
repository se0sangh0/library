<?php
session_start();
// --- 관리자 인증 가드 ---
if (empty($_SESSION['is_admin']) || $_SESSION['is_admin'] !== true) {
    die("오류: 접근 권한이 없습니다.");
}
$c_program_path = "C:/xampp/htdocs/library/library_project/library_cli/x64/Debug/library_cli.exe"; 

// 실행할 명령어
$command = 'chcp 65001 > NUL && ' . $c_program_path . ' --approve';

// C 프로그램 실행 및 결과 캡처
$output = shell_exec($command . ' 2>&1'); // 2>&1는 표준 에러도 함께 캡처합니다.
$output = mb_convert_encoding($output, 'UTF-8','CP949');
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>승인 처리 결과</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .result-box {
            background-color: #333;
            color: #fff;
            padding: 20px;
            border-radius: 5px;
            font-family: 'Courier New', Courier, monospace;
            white-space: pre-wrap; /* 줄바꿈 및 공백 유지 */
            text-align: left;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>승인 처리 실행 결과</h1>
    <p><a href="admin.php">관리자 페이지로 돌아가기</a></p>
    <hr>
    <h3>C 프로그램 실행 결과:</h3>
    <div class="result-box">
        <?php
        if ($output === null) {
            echo "프로그램 실행에 실패했습니다.\n";
            echo "C 프로그램 경로가 올바른지 확인해주세요.\n";
            echo "경로: " . htmlspecialchars($c_program_path);
        } else {
            echo htmlspecialchars($output);
        }
        ?>
    </div>
</div>
</body>
</html>