#include "a.h"
/**
 * @brief ���� ������ ó���ϴ� �Լ�
 */
void handle_borrow(MYSQL* conn, const char* user_id_str, const char* book_id_str) {
    char query[512];
    char log_message[256];

    // 1. å ���� Ȯ�� �� ���� ó�� (Ʈ����� ����)
    mysql_query(conn, "START TRANSACTION");

    // 2. å ���¸� 'on_loan'���� �����ϰ�, ���� Ƚ�� 1 ����
    snprintf(query, sizeof(query),
        "UPDATE books SET status = 'on_loan', loan_count = loan_count + 1 WHERE book_id = %s AND status = 'available'",
        book_id_str);

    if (mysql_query(conn, query)) {
        fprintf(stderr, "Query ����: %s\n", mysql_error(conn));
        mysql_query(conn, "ROLLBACK"); // ���� �߻� �� �ѹ�
        return;
    }

    // ������ ����� ���� ���� 0�̸�, �̹� ���� ���̰ų� ���� å
    if (mysql_affected_rows(conn) == 0) {
        fprintf(stderr, "�̹� �������̰ų�, �������� �ʴ� ���� �Դϴ�..\n");
        write_log("���� ����");
        mysql_query(conn, "ROLLBACK");
        return;
    }

    // 3. loans ���̺� ���� ��� ���� (���� �Ⱓ 2��)
    snprintf(query, sizeof(query),
        "INSERT INTO loans (book_id, user_id, loan_date, due_date) VALUES (%s, %s, NOW(), DATE_ADD(NOW(), INTERVAL 14 DAY))",
        book_id_str, user_id_str);

    if (mysql_query(conn, query)) {
        fprintf(stderr, "Query ���� �߻� : %s\n", mysql_error(conn));
        mysql_query(conn, "ROLLBACK"); // ���� �߻� �� �ѹ�
        return;
    }

    // 4. ��� ������ ���������� Ŀ��
    mysql_query(conn, "COMMIT");

    fprintf(stdout,"Book ID %s ���⿡ �����Ͽ����ϴ�. ����� ���� : %s.\n", book_id_str, user_id_str);

    // 5. ���� �α� ���
    snprintf(log_message, sizeof(log_message), "���� ����: ����� %s ���⵵�� %s.", user_id_str, book_id_str);
    write_log(log_message);
}