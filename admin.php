<?php
session_start();
include 'config.php';

// --- 관리자 인증 가드 ---
if (empty($_SESSION['is_admin']) || $_SESSION['is_admin'] !== true) {
    // is_admin 세션 변수가 없거나 true가 아니면 접근 불가
    die("오류: 접근 권한이 없습니다.");
}

// 'pending' 상태인 신청 목록 가져오기
$requests = [];
$sql = "SELECT r.request_id, r.book_title, r.book_author, u.username as request_user, r.request_date 
        FROM book_requests r
        JOIN users u ON r.user_id = u.user_id
        WHERE r.status = 'pending'
        ORDER BY r.request_date ASC";
$result = $conn->query($sql);
while ($row = $result->fetch_assoc()) {
    $requests[] = $row;
}
$conn->close();
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 페이지</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container">
    <h1>관리자 페이지</h1>
    <p><a href="index.php">메인으로 돌아가기</a></p>
    <hr>

    <h2>신규 도서 신청 목록 (처리 대기중)</h2>
    <div class="admin-instruction">
        <p>아래 목록의 신청 건들을 처리하려면, 서버의 터미널(명령 프롬프트)에서<br>
        C언어로 작성된 관리 프로그램을 실행해주세요.</p>
        <code>library_cli --approve</code>
    </div>

    <?php if (!empty($requests)): ?>
        <table style="width: 100%; margin-top: 20px;">
            <thead>
                <tr>
                    <th>신청번호</th>
                    <th>도서명</th>
                    <th>저자</th>
                    <th>신청자</th>
                    <th>신청일</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($requests as $req): ?>
                    <tr>
                        <td><?php echo $req['request_id']; ?></td>
                        <td><?php echo htmlspecialchars($req['book_title']); ?></td>
                        <td><?php echo htmlspecialchars($req['book_author']); ?></td>
                        <td><?php echo htmlspecialchars($req['request_user']); ?></td>
                        <td><?php echo $req['request_date']; ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p style="text-align:center; padding: 20px;">처리 대기 중인 새로운 도서 신청이 없습니다.</p>
    <?php endif; ?>
</div>
</body>
</html>