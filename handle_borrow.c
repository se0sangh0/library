#include "a.h"
/**
 * @brief 도서 대출을 처리하는 함수
 */
void handle_borrow(MYSQL* conn, const char* user_id_str, const char* book_id_str) {
    char query[512];
    char log_message[256];

    // 1. 책 상태 확인 및 대출 처리 (트랜잭션 시작)
    mysql_query(conn, "START TRANSACTION");

    // 2. 책 상태를 'on_loan'으로 변경하고, 대출 횟수 1 증가
    snprintf(query, sizeof(query),
        "UPDATE books SET status = 'on_loan', loan_count = loan_count + 1 WHERE book_id = %s AND status = 'available'",
        book_id_str);

    if (mysql_query(conn, query)) {
        fprintf(stderr, "Query 오류: %s\n", mysql_error(conn));
        mysql_query(conn, "ROLLBACK"); // 오류 발생 시 롤백
        return;
    }

    // 쿼리로 변경된 행의 수가 0이면, 이미 대출 중이거나 없는 책
    if (mysql_affected_rows(conn) == 0) {
        fprintf(stderr, "이미 대출중이거나, 존재하지 않는 도서 입니다..\n");
        write_log("대출 실패");
        mysql_query(conn, "ROLLBACK");
        return;
    }

    // 3. loans 테이블에 대출 기록 삽입 (대출 기간 2주)
    snprintf(query, sizeof(query),
        "INSERT INTO loans (book_id, user_id, loan_date, due_date) VALUES (%s, %s, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY))",
        book_id_str, user_id_str);

    if (mysql_query(conn, query)) {
        fprintf(stderr, "Query 오류 발생 : %s\n", mysql_error(conn));
        mysql_query(conn, "ROLLBACK"); // 오류 발생 시 롤백
        return;
    }

    // 4. 모든 쿼리가 성공했으면 커밋
    mysql_query(conn, "COMMIT");

    fprintf(stdout,"Book ID %s 대출에 성공하였습니다. 사용자 정보 : %s.\n", book_id_str, user_id_str);

    // 5. 성공 로그 기록
    snprintf(log_message, sizeof(log_message), "대출 성공: 사용자 %s 대출도서 %s.", user_id_str, book_id_str);
    write_log(log_message);
}