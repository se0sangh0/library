#include "a.h"
/**
 * @brief �����ͺ��̽� ������ �����ϴ� �Լ� (���� ���)
 * @return MYSQL ���� ��ü ������, ���� �� NULL
 */
MYSQL* db_connect() {
    MYSQL* conn = mysql_init(NULL);
    if (conn == NULL) {
        fprintf(stderr, "mysql_init() failed\n");
        return NULL;
    }
    if (mysql_real_connect(conn, DB_HOST, DB_USER, DB_PASS, DB_NAME, 0, NULL, 0) == NULL) {
        fprintf(stderr, "mysql_real_connect() failed: %s\n", mysql_error(conn));
        mysql_close(conn);
        return NULL;
    }
    // �ѱ� ���� ������ ���� UTF-8�� ����
    mysql_set_character_set(conn, "utf8mb4");
    return conn;
}