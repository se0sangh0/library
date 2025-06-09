<?php
session_start();
include 'config.php';

// --- 인증 가드 ---
// 로그인을 하지 않은 사용자는 이 페이지에 접근할 수 없습니다.
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

$user_id = $_SESSION['user_id'];

// --- 1. 현재 대출 중인 도서 목록 가져오기 ---
$current_loans = [];
// loans 테이블과 books 테이블을 JOIN 하여 필요한 정보를 가져옵니다.
$sql_current = "SELECT b.title, b.author, b.book_number, l.due_date
                FROM loans l
                JOIN books b ON l.book_id = b.book_id
                WHERE l.user_id = ? AND l.return_date IS NULL
                ORDER BY l.due_date ASC";

$stmt_current = $conn->prepare($sql_current);
$stmt_current->bind_param("i", $user_id);
$stmt_current->execute();
$result_current = $stmt_current->get_result();
while ($row = $result_current->fetch_assoc()) {
    $current_loans[] = $row;
}
$stmt_current->close();


// --- 2. 과거 대출 기록(반납 완료) 가져오기 ---
$loan_history = [];
$sql_history = "SELECT b.title, b.author, b.book_number, l.return_date
                FROM loans l
                JOIN books b ON l.book_id = b.book_id
                WHERE l.user_id = ? AND l.return_date IS NOT NULL
                ORDER BY l.return_date DESC";

$stmt_history = $conn->prepare($sql_history);
$stmt_history->bind_param("i", $user_id);
$stmt_history->execute();
$result_history = $stmt_history->get_result();
while ($row = $result_history->fetch_assoc()) {
    $loan_history[] = $row;
}
$stmt_history->close();

$conn->close();
?>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <style>
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h1>마이페이지</h1>
    <p><strong><?php echo htmlspecialchars($_SESSION['name']); ?></strong>님의 이용 내역입니다.</p>
    <p><a href="index.php">메인으로 돌아가기</a></p>
    <hr>

    <h2>현재 대출 중인 도서</h2>
    <table>
        <thead>
            <tr>
                <th>도서명</th>
                <th>저자</th>
                <th>도서번호</th>
                <th>반납 기한</th>
                <th>남은 기간</th>
            </tr>
        </thead>
        <tbody>
            <?php if (empty($current_loans)): ?>
                <tr><td colspan="5">대출 중인 도서가 없습니다.</td></tr>
            <?php else: ?>
                <?php foreach ($current_loans as $loan): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($loan['title']); ?></td>
                        <td><?php echo htmlspecialchars($loan['author']); ?></td>
                        <td><?php echo htmlspecialchars($loan['book_number']); ?></td>
                        <td><?php echo date("Y-m-d", strtotime($loan['due_date'])); ?></td>
                        <td>
                            <?php
                                $today = new DateTime();
                                $due_date_obj = new DateTime($loan['due_date']);
                                if ($today > $due_date_obj) {
                                    echo "<strong style='color:red;'>연체되었습니다</strong>";
                                } else {
                                    $interval = $today->diff($due_date_obj);
                                    echo $interval->days . "일 남음";
                                }
                            ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            <?php endif; ?>
        </tbody>
    </table>

    <h2>나의 대출 기록 (반납 완료)</h2>
    <table>
        <thead>
            <tr>
                <th>도서명</th>
                <th>저자</th>
                <th>도서번호</th>
                <th>반납일</th>
            </tr>
        </thead>
        <tbody>
            <?php if (empty($loan_history)): ?>
                <tr><td colspan="4">반납한 도서가 없습니다.</td></tr>
            <?php else: ?>
                <?php foreach ($loan_history as $history): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($history['title']); ?></td>
                        <td><?php echo htmlspecialchars($history['author']); ?></td>
                        <td><?php echo htmlspecialchars($history['book_number']); ?></td>
                        <td><?php echo date("Y-m-d", strtotime($history['return_date'])); ?></td>
                    </tr>
                <?php endforeach; ?>
            <?php endif; ?>
        </tbody>
    </table>

</body>
</html>