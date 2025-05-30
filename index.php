<?php
session_start(); // 항상 최상단에 호출
include 'config.php'; // DB 연결을 위해 추가
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>작은 도서관</title>
</head>
<body>
    <h1>작은 도서관에 오신 것을 환영합니다!</h1>
    <hr>
    <div>
        <?php if (isset($_SESSION['username'])): 
            //if (isset($_SESSION['username'])): 코드를 통해 로그인 상태를 확인하고, 그에 따라 다른 HTML 블록을 보여줍니다. 
            ?>
            <p><strong><?php echo htmlspecialchars($_SESSION['name']); ?></strong>님, 환영합니다.</p>
            <a href="mypage.php">마이페이지</a> |
            <a href="search.php">도서 검색</a> |
            <a href="logout.php">로그아웃</a>
            <?php /* if ($_SESSION['is_admin']): */ ?>
                <?php /* endif; */ ?>
        <?php else: ?>
            <p>서비스를 이용하려면 로그인이 필요합니다.</p>
            <a href="login.php">로그인</a> |
            <a href="join.php">회원가입</a>
        <?php endif; ?>
    </div>
    <hr>
    
    <h2>신착 도서 목록</h2>
    <ul>
        <?php
        // DB에서 added_date(추가된 날짜)를 기준으로 최신 도서 10권을 조회하는 쿼리
        $sql = "SELECT title, author FROM books ORDER BY added_date DESC LIMIT 10";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            // 결과가 있으면 각 행(row)을 목록(li)으로 출력
            while($row = $result->fetch_assoc()) {
                echo "<li>" . htmlspecialchars($row["title"]) . " - " . htmlspecialchars($row["author"]) . "</li>";
            }
        } else {
            echo "<li>신착 도서가 없습니다.</li>";
        }
        $conn->close(); // DB 연결 종료
        ?>
    </ul>
    <hr>

    <h2>도서 대출 랭킹</h2>
    <hr>

    <h2>공지사항</h2>

</body>
</html>