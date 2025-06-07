#include "a.h"

/**
 * @brief 사용법을 출력하는 함수
 */
void print_usage() {
    fprintf(stdout,"사용방법:\n");
    fprintf(stdout, ":\n");
    fprintf(stdout, "  library_cli --borrow --user_id <USER_ID> --book_id <BOOK_ID>\n");
    fprintf(stdout, "  library_cli --return --book_id <BOOK_ID>\n");

}