#include "a.h"
void handle_return(MYSQL* conn, const char* book_id_str) {
    char query[512];
    char log_message[256];

    // TODO: 연체 여부를 확인하고 패널티를 부여하는 로직은 추후 이 부분에 추가합니다.

    // 1. books 테이블의 상태를 'available'로 변경
    snprintf(query, sizeof(query), "UPDATE books SET status = 'available' WHERE book_id = %s", book_id_str);
    if (mysql_query(conn, query) || mysql_affected_rows(conn) == 0) {
        fprintf(stderr, "Error: Failed to update book status or book does not exist.\n");
        write_log("RETURN FAILED: Book status update failed.");
        return;
    }

    // 2. loans 테이블의 해당 레코드에 return_date를 현재 시간으로 기록
    snprintf(query, sizeof(query), "UPDATE loans SET return_date = NOW() WHERE book_id = %s AND return_date IS NULL", book_id_str);
    if (mysql_query(conn, query)) {
        fprintf(stderr, "Error: Failed to update loan record.\n");
        // 이 경우 이미 book 상태는 변경되었으므로 롤백 처리가 필요할 수 있으나, 지금은 단순하게 구현합니다.
        return;
    }

    printf("Book ID %s has been successfully returned.\n", book_id_str);

    // 3. 성공 로그 기록
    snprintf(log_message, sizeof(log_message), "RETURN SUCCESS: Book %s was returned.", book_id_str);
    write_log(log_message);
}