#include "a.h"
void handle_return(MYSQL* conn, const char* book_id_str) {
    char query[512];
    char log_message[256];

    // TODO: ��ü ���θ� Ȯ���ϰ� �г�Ƽ�� �ο��ϴ� ������ ���� �� �κп� �߰��մϴ�.

    // 1. books ���̺��� ���¸� 'available'�� ����
    snprintf(query, sizeof(query), "UPDATE books SET status = 'available' WHERE book_id = %s", book_id_str);
    if (mysql_query(conn, query) || mysql_affected_rows(conn) == 0) {
        fprintf(stderr, "Error: Failed to update book status or book does not exist.\n");
        write_log("RETURN FAILED: Book status update failed.");
        return;
    }

    // 2. loans ���̺��� �ش� ���ڵ忡 return_date�� ���� �ð����� ���
    snprintf(query, sizeof(query), "UPDATE loans SET return_date = NOW() WHERE book_id = %s AND return_date IS NULL", book_id_str);
    if (mysql_query(conn, query)) {
        fprintf(stderr, "Error: Failed to update loan record.\n");
        // �� ��� �̹� book ���´� ����Ǿ����Ƿ� �ѹ� ó���� �ʿ��� �� ������, ������ �ܼ��ϰ� �����մϴ�.
        return;
    }

    printf("Book ID %s has been successfully returned.\n", book_id_str);

    // 3. ���� �α� ���
    snprintf(log_message, sizeof(log_message), "RETURN SUCCESS: Book %s was returned.", book_id_str);
    write_log(log_message);
}