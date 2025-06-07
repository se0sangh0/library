#include "a.h"

// --- 로그 파일 경로 (전역 변수로 변경) ---
const char* LOG_DIR = "C:/xampp/htdocs/library/logs";
const char* LOG_FILE = "C:/xampp/htdocs/library/logs/activity.log";

int main(int argc, char* argv[]) {
    // 프로그램 시작 시 가장 먼저 로깅 시스템을 초기화합니다.
    if (setup_logging() != 0) {
        fprintf(stderr, "로깅 시스템 초기화 오류.\n");
        return 1; // 로깅 시스템 초기화 실패 시 프로그램 종료
    }
    // --- 이후 로직은 이전과 동일 ---
    if (argc < 2) {
        print_usage();
        return 1;
    }

    MYSQL* conn = db_connect();
    if (conn == NULL) {
        return 1; // DB 연결 실패 시 종료
    }

    // 첫 번째 인자(명령)에 따라 분기
    if (strcmp(argv[1], "--borrow") == 0) {
        if (argc != 6 || strcmp(argv[2], "--user_id") != 0 || strcmp(argv[4], "--book_id") != 0) {
            fprintf(stderr, "--borrow에 대한 잘못된 인자가 전달되었습니다.\n");
            print_usage();
        }
        else {
            handle_borrow(conn, argv[3], argv[5]);
        }
    }
    else if (strcmp(argv[1], "--return") == 0) {
        if (argc != 4 || strcmp(argv[2], "--book_id") != 0) {
            fprintf(stderr, "Error: Incorrect arguments for --return\n");
            print_usage();
        }
        else {
            handle_return(conn, argv[3]); // handle_return 함수 호출
        }
       
    }
    else {
        fprintf(stderr, "잘못된 명령어 입니다. '%s'\n", argv[1]);
        print_usage();
    }

    mysql_close(conn);
    return 0;
}










