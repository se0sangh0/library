#include "a.h"

/**
 * @brief �α� ���Ͽ� �޽����� ����ϴ� �Լ� (���� �����)
 * @param message �α׿� ���� �޽���
 */
void write_log(const char* message) {
    // ���� �� �Լ��� ȣ��� ���� �׻� �α� ���丮�� �������� ����˴ϴ�.
    FILE* fp = fopen(LOG_FILE, "a");
    if (fp == NULL) {
        fprintf(stderr,"�α� ���� ���࿡ �����Ͽ����ϴ�.");
        return;
    }

    time_t now = time(NULL);
    struct tm* t = localtime(&now);

    // �α� ����: [YYYY-MM-DD HH:MM:SS] �޽���
    fprintf(fp, "[%04d-%02d-%02d %02d:%02d:%02d] %s\n",
        t->tm_year + 1900, t->tm_mon + 1, t->tm_mday,
        t->tm_hour, t->tm_min, t->tm_sec, message);

    fclose(fp);
    /**
 * @brief �����ͺ��̽� ������ �����ϴ� �Լ� (���� ���)
 * @return MYSQL ���� ��ü ������, ���� �� NULL
 */
    
}