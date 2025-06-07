#include "a.h"

/**
 * @brief 로그 파일에 메시지를 기록하는 함수 (파일 입출력)
 * @param message 로그에 남길 메시지
 */
void write_log(const char* message) {
    // 이제 이 함수가 호출될 때는 항상 로그 디렉토리가 존재함이 보장됩니다.
    FILE* fp = fopen(LOG_FILE, "a");
    if (fp == NULL) {
        fprintf(stderr,"로그 파일 실행에 실패하였습니다.");
        return;
    }

    time_t now = time(NULL);
    struct tm* t = localtime(&now);

    // 로그 형식: [YYYY-MM-DD HH:MM:SS] 메시지
    fprintf(fp, "[%04d-%02d-%02d %02d:%02d:%02d] %s\n",
        t->tm_year + 1900, t->tm_mon + 1, t->tm_mday,
        t->tm_hour, t->tm_min, t->tm_sec, message);

    fclose(fp);
    /**
 * @brief 데이터베이스 서버에 연결하는 함수 (서버 사용)
 * @return MYSQL 연결 객체 포인터, 실패 시 NULL
 */
    
}