<?php
session_start();
// 관리자만 접근 가능하도록 설정 (필요시 user_type 등으로 조건 변경)
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

$search_results = [];
$search_term = '';

// 폼이 제출되었을 때
if (isset($_GET['query'])) {
    $search_term = trim($_GET['query']);
    if (!empty($search_term)) {
        $rest_api_key = "8be68daf705d22b27e6d2cdddc1e9535"; 

        $enc_text = urlencode($search_term);
        // 카카오 API는 target 파라미터로 검색 대상을 지정할 수 있습니다.
        $api_url = "https://dapi.kakao.com/v3/search/book?target=title&query=".$enc_text;

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $api_url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            "Authorization: KakaoAK ".$rest_api_key // 인증 헤더 형식 변경
        ]);
        $response = curl_exec($ch);
        $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($http_code == 200) {
            $response_data = json_decode($response, true);
            $search_results = $response_data['documents']; // 결과 배열 이름 변경 (items -> documents)
        } else {
            echo "API 호출에 실패했습니다. 응답 코드: " . $http_code;
            // print_r($response); // 오류 원인 파악을 위해 주석 해제 후 확인
        }
    }
}
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>신규 도서 등록 (Kakao API)</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .book-item { text-align: left; border-bottom: 1px solid #eee; padding: 15px 0; display: flex; }
        .book-item img { width: 80px; margin-right: 20px; border: 1px solid #ddd; }
        .book-info { flex-grow: 1; }
        .book-info h3 { margin-top: 0; }
    </style>
</head>
<body>
    <div class="container">
        <h1>신규 도서 등록 (Kakao API 연동)</h1>
        <p><a href="index.php">메인으로 돌아가기</a></p>

        <form action="add_book.php" method="GET">
            <input type="text" name="query" placeholder="책 제목으로 검색" value="<?php echo htmlspecialchars($search_term); ?>" required style="width: 70%;">
            <button type="submit" class="btn btn-primary">카카오에서 검색</button>
        </form>
        <hr>

        <h2>검색 결과</h2>
        <div class="results-list">
            <?php if (!empty($search_results)): ?>
                <?php foreach ($search_results as $book): ?>
                    <div class="book-item">
                        <img src="<?php echo !empty($book['thumbnail']) ? htmlspecialchars($book['thumbnail']) : 'https://via.placeholder.com/80x115.png?text=No+Image'; ?>" alt="책 표지">
                        <div class="book-info">
                            <h3><?php echo htmlspecialchars($book['title']); ?></h3>
                            <p>
                                <strong>저자:</strong> <?php echo htmlspecialchars(implode(', ', $book['authors'])); ?> |
                                <strong>출판사:</strong> <?php echo htmlspecialchars($book['publisher']); ?>
                            </p>
                            <p>
                                <strong>출간일:</strong> <?php echo htmlspecialchars(substr($book['datetime'], 0, 10)); ?> |
                                <strong>ISBN:</strong> <?php echo htmlspecialchars($book['isbn']); ?>
                            </p>
                            <p><?php echo htmlspecialchars($book['contents']); ?>...</p>
                            
                            <form action="add_book_process.php" method="POST">
                                <input type="hidden" name="title" value="<?php echo htmlspecialchars($book['title']); ?>">
                                <input type="hidden" name="author" value="<?php echo htmlspecialchars(implode(', ', $book['authors'])); ?>">
                                <input type="hidden" name="publisher" value="<?php echo htmlspecialchars($book['publisher']); ?>">
                                <input type="hidden" name="pubdate" value="<?php echo htmlspecialchars(substr($book['datetime'], 0, 10)); ?>">
                                <input type="hidden" name="isbn" value="<?php echo htmlspecialchars($book['isbn']); ?>">
                                <input type="hidden" name="image" value="<?php echo htmlspecialchars($book['thumbnail']); ?>">
                                <input type="hidden" name="description" value="<?php echo htmlspecialchars($book['contents']); ?>">
                                <button type="submit" class="btn btn-reserve">이 책 등록하기</button>
                            </form>
                        </div>
                    </div>
                <?php endforeach; ?>
            <?php else: ?>
                <p>검색어를 입력하여 도서를 찾아보세요.</p>
            <?php endif; ?>
        </div>
    </div>
</body>
</html>