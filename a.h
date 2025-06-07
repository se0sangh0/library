#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <mysql.h>
#include <sys/stat.h> // stat 함수를 사용하기 위한 헤더 파일이나 디렉토리의 정보를 얻어오는 함수
#include <direct.h>   // _mkdir 함수를 사용하기 위한 헤더 (Windows용 디렉토리를 생성)

// --- DB 연결 정보 (이전과 동일) ---
#define DB_HOST "localhost"
#define DB_USER "root"
#define DB_PASS ""
#define DB_NAME "library_db"

// extern 키워드를 사용하여 전역 변수가 다른 곳에 '정의'되어 있음을 '선언'합니다.
// 여기서는 실제 값을 할당하지 않습니다.
extern const char* LOG_DIR;
extern const char* LOG_FILE;


// --- 함수 프로토타입 선언 ---
int setup_logging(); // 새로 추가된 함수
void write_log(const char*);
MYSQL* db_connect();
void handle_borrow(MYSQL* , const char* , const char* );
void print_usage();
void handle_return(MYSQL* , const char* ); 

