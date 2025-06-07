#include "a.h"

int setup_logging() {
    struct stat st = { 0 };

    // 1. stat 함수로 로그 디렉토리의 정보를 가져옵니다.
    //    디렉토리가 존재하지 않으면 stat 함수는 -1을 반환합니다.
    if (stat(LOG_DIR, &st) == -1) {
        fprintf(stderr,"지정된 경로가 존재하지 않습니다. 새 경로를 생성합니다.: %s\n", LOG_DIR);

        // 2. _mkdir 함수로 디렉토리를 생성합니다.
        if (_mkdir(LOG_DIR) != 0) {
            perror("경로 생성에 실패하였습니다.");
            return -1; // 생성 실패
        }
    }
    // 디렉토리 생성에 성공했거나 이미 존재하면 0을 반환합니다.
    return 0; // 성공
}