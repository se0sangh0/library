#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <mysql.h>
#include <sys/stat.h> // stat �Լ��� ����ϱ� ���� ��� �����̳� ���丮�� ������ ������ �Լ�
#include <direct.h>   // _mkdir �Լ��� ����ϱ� ���� ��� (Windows�� ���丮�� ����)

// --- DB ���� ���� (������ ����) ---
#define DB_HOST "localhost"
#define DB_USER "root"
#define DB_PASS ""
#define DB_NAME "library_db"

// extern Ű���带 ����Ͽ� ���� ������ �ٸ� ���� '����'�Ǿ� ������ '����'�մϴ�.
// ���⼭�� ���� ���� �Ҵ����� �ʽ��ϴ�.
extern const char* LOG_DIR;
extern const char* LOG_FILE;


// --- �Լ� ������Ÿ�� ���� ---
int setup_logging(); // ���� �߰��� �Լ�
void write_log(const char*);
MYSQL* db_connect();
void handle_borrow(MYSQL* , const char* , const char* );
void print_usage();
void handle_return(MYSQL* , const char* ); 

