<?php
session_start();
include 'config.php';

// --- 인증 가드 ---
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

$search_term = isset($_GET['query']) ? trim($_GET['query']) : '';
$search_type = isset($_GET['search_type']) ? $_GET['search_type'] : 'title';
$local_results = [];
$api_results = [];

// 검색어가 있을 경우에만 실행
if (!empty($search_term)) {

    // 1. 우리 도서관 DB에서 검색
    $stmt = $conn->prepare("SELECT book_id, book_number, title, author, status, isbn FROM books WHERE title LIKE ?");
    $like_term = "%" . $search_term . "%";
    $stmt->bind_param("s", $like_term);
    $stmt->execute();
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        $local_results[$row['isbn']] = $row; // ISBN을 키로 사용하여 중복 확인 용이하게 함
    }
    $stmt->close();

    // 2. 카카오 API에서 검색
    $rest_api_key = "8be68daf705d22b27e6d2cdddc1e9535"; // ◀◀◀ 여기에 카카오 REST API 키를 입력하세요!
    $enc_text = urlencode($search_term);
    $api_url = "https://dapi.kakao.com/v3/search/book?target=title&query=".$enc_text;

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ["Authorization: KakaoAK ".$rest_api_key]);
    $response = curl_exec($ch);
    $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($http_code == 200) {
        $response_data = json_decode($response, true);
        $api_results = $response_data['documents'];
    }
}
$conn->close();
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>통합 도서 검색</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .book-item { text-align: left; border-bottom: 1px solid #eee; padding: 15px 0; display: flex; align-items: center; }
        .book-item img { width: 60px; height: auto; margin-right: 20px; border: 1px solid #ddd; }
        .book-info { flex-grow: 1; }
        .book-info h4 { margin: 0 0 5px 0; }
        .book-action { width: 100px; text-align: center; }
        .status { font-weight: bold; }
        .status-available { color: var(--success-color); }
        .status-onloan { color: var(--error-color); }
    </style>
</head>
<body>
<div class="container">
    <h1>통합 도서 검색</h1>
    <p><a href="index.php">메인으로 돌아가기</a></p>

    <form action="search.php" method="GET">
        <input type="text" name="query" placeholder="도서명으로 검색" value="<?php echo htmlspecialchars($search_term); ?>" required style="width: 70%;">
        <button type="submit" class="btn btn-primary">검색</button>
    </form>
    <hr>

    <h2>검색 결과</h2>

    <?php if (!empty($local_results)): ?>
        <h3>📚 소장 도서</h3>
        <?php foreach ($local_results as $book): ?>
            <div class="book-item">
                <div class="book-info">
                    <h4><?php echo htmlspecialchars($book['title']); ?></h4>
                    <p>저자: <?php echo htmlspecialchars($book['author']); ?></p>
                    <p>도서 번호 : <?php echo htmlspecialchars($book['book_number']); ?></p>
                </div>
                <div class="book-action">
                    <?php if ($book['status'] === 'available'): ?>
                        <span class="status status-available">대출 가능</span>
                        <?php elseif ($book['status'] === 'on_loan'): ?>
                        <span class="status status-onloan">대출중</span>
                        <form action="reserve_process.php" method="POST" style="margin-top:5px;">
                            <input type="hidden" name="book_id" value="<?php echo htmlspecialchars($book['book_id']); ?>">
                            <button type="submit" class="btn btn-reserve">예약</button>
                        </form>
                    <?php endif; ?>
                </div>
            </div>
        <?php endforeach; ?>
    <?php endif; ?>


   <?php if (!empty($api_results)): ?>
        <h3>📖 외부 도서 (신청 가능)</h3>
        
        <?php foreach ($api_results as $book): ?>
            <?php
            // ISBN 정보가 없거나, 이미 우리 DB에 있는 책이면 건너뛰기
            // 카카오 API의 ISBN은 공백으로 구분된 2개의 번호를 줄 수 있으므로, 뒤의 번호(ISBN-13)를 사용
            $isbn = !empty($book['isbn']) ? explode(' ', $book['isbn'])[1] : ''; 
            if (empty($isbn) || isset($local_results[$isbn])) {
                continue;
            }
            ?>
            <div class="book-item">
                <img src="<?php echo !empty($book['thumbnail']) ? htmlspecialchars($book['thumbnail']) : ''; ?>" alt="표지">
                <div class="book-info">
                    <h4><?php echo htmlspecialchars($book['title']); ?></h4>
                    <p>저자: <?php echo htmlspecialchars(implode(', ', $book['authors'])); ?></p>
                </div>
                <div class="book-action">
                    <form action="request_process.php" method="POST">
                        <input type="hidden" name="title" value="<?php echo htmlspecialchars($book['title']); ?>">
                        <input type="hidden" name="author" value="<?php echo htmlspecialchars(implode(', ', $book['authors'])); ?>">
                        <input type="hidden" name="publisher" value="<?php echo htmlspecialchars($book['publisher']); ?>">
                        <input type="hidden" name="pubdate" value="<?php echo htmlspecialchars(substr($book['datetime'], 0, 10)); ?>">
                        <input type="hidden" name="isbn" value="<?php echo htmlspecialchars($isbn); ?>">
                        <input type="hidden" name="image" value="<?php echo htmlspecialchars($book['thumbnail']); ?>">
                        <input type="hidden" name="description" value="<?php echo htmlspecialchars($book['contents']); ?>">
                        <button type="submit" class="btn btn-secondary">신청하기</button>
                    </form>
                </div>
            </div>
        <?php endforeach; ?>
    <?php endif; ?>

    <?php if (empty($local_results) && empty($api_results) && !empty($search_term)): ?>
        <p>"<?php echo htmlspecialchars($search_term); ?>"에 대한 검색 결과가 없습니다.</p>
    <?php endif; ?>
</div>
</body>
</html>