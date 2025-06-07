#include "a.h"
/**
 * @brief 데이터베이스 서버에 연결하는 함수 (서버 사용)
 * @return MYSQL 연결 객체 포인터, 실패 시 NULL
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
    // 한글 깨짐 방지를 위해 UTF-8로 설정
    mysql_set_character_set(conn, "utf8mb4");
    return conn;
}