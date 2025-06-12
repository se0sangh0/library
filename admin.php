<?php
session_start();
include 'config.php';

// --- 관리자 인증 가드 ---
if (empty($_SESSION['is_admin']) || $_SESSION['is_admin'] !== true) {
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
    <style>
        .admin-action-box {
            background-color: #eef7ff;
            border: 1px solid #cce0ff;
            padding: 20px;
            margin: 20px 0;
            text-align: center;
            border-radius: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>관리자 페이지</h1>
    <p><a href="index.php">메인으로 돌아가기</a></p>
    <hr>
<h2>도서 대출 처리</h2>
<div class="admin-action-box">
    <form action="run_borrow_return_process.php" method="POST">
        <input type="hidden" name="action" value="borrow">
        
        <p>
            <label for="borrow_user_id">사용자 ID:</label><br>
            <input type="text" id="borrow_user_id" name="user_id" placeholder="대출할 사용자의 user_id 입력" required>
        </p>
        <p>
            <label for="borrow_book_id">도서 관리번호:</label><br>
            <input type="text" id="borrow_book_number" name="book_number" placeholder="대출할 도서의 관리번호 (예: LIB-00001) 입력" required>
        </p>
        <button type="submit" class="btn btn-primary">대출 실행</button>
    </form>
</div>

<hr>
<h2>도서 반납 처리</h2>
<div class="admin-action-box">
    <form action="run_borrow_return_process.php" method="POST">
        <input type="hidden" name="action" value="return">
        <p>
            <label for="return_book_number">도서 관리번호:</label><br>
<input type="text" id="return_book_number" name="book_number" placeholder="반납할 도서의 관리번호 (예: LIB-00001) 입력" required>
        </p>
        <button type="submit" class="btn btn-secondary">반납 실행</button>
    </form>
</div>
    <hr>
    <h2>신규 도서 신청 목록 (처리 대기중)</h2>
    
    <div class="admin-action-box">
        <?php if (!empty($requests)): ?>
            <p><strong>총 <?php echo count($requests); ?>건</strong>의 처리 대기 중인 신청이 있습니다.</p>
            <form action="run_approve_process.php" method="POST">
                <button type="submit" class="btn btn-primary">신청 일괄 승인 처리 실행</button>
            </form>
        <?php else: ?>
            <p>처리 대기 중인 새로운 도서 신청이 없습니다.</p>
        <?php endif; ?>
    </div>

    <?php if (!empty($requests)): ?>
        <table style="width: 100%;">
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
    <?php endif; ?>
</div>
</body>
</html>