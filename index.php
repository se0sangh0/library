<?php
session_start(); // 항상 최상단에 호출
include 'config.php'; // DB 연결을 위해 추가
?>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>작은 도서관</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <div class="container">
        <?php
        // 관리자 여부 확인
        if (!empty($_SESSION['is_admin']) && $_SESSION['is_admin'] === true) {
            // 관리자일 경우: admin.php로 이동하는 링크가 있는 이미지 출력
            echo '<a href="admin.php" title="관리자 페이지로 이동">';
            echo '    <img src="character.png" width="50" height="50" alt="도서관 캐릭터" class="header-character">';
            echo '</a>';
        } else {
            // 일반 사용자일 경우: 링크가 없는 일반 이미지 출력
            echo '<img src="character.png" width="50" height="50" alt="도서관 캐릭터" class="header-character">';
        }
        ?>
        <h1>작은 도서관에 오신 것을 환영합니다!</h1>
    <hr>
    <div class="navigation">
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
        ?>
    </ul>
    <hr>

    <h2>도서 대출 랭킹</h2>
    <ol>
        <?php
        // loan_count(대출 횟수)를 기준으로 상위 10권의 도서를 조회하는 쿼리
        $rank_sql = "SELECT title, author, loan_count FROM books ORDER BY loan_count DESC, title ASC LIMIT 10";
        $rank_result = $conn->query($rank_sql);

        if ($rank_result->num_rows > 0) {
            while($rank_row = $rank_result->fetch_assoc()) {
                // 기획서 요구사항에 따라 대출 횟수는 화면에 표시하지 않습니다.
                echo "<li>" . htmlspecialchars($rank_row["title"]) . " - " . htmlspecialchars($rank_row["author"]) . "</li>";
            }
        } else {
            echo "<li>대출 기록이 있는 도서가 없습니다.</li>";
        }
        ?>
    </ol>
    <hr>
    
    <h2>공지사항</h2>
    <?php
    // 페이지의 모든 DB 작업이 끝났으므로 여기서 연결을 닫습니다.
    $conn->close();
    ?>
    </div>
</body>
</html>