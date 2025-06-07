#include "a.h"

// --- �α� ���� ��� (���� ������ ����) ---
const char* LOG_DIR = "C:/xampp/htdocs/library/logs";
const char* LOG_FILE = "C:/xampp/htdocs/library/logs/activity.log";

int main(int argc, char* argv[]) {
    // ���α׷� ���� �� ���� ���� �α� �ý����� �ʱ�ȭ�մϴ�.
    if (setup_logging() != 0) {
        fprintf(stderr, "�α� �ý��� �ʱ�ȭ ����.\n");
        return 1; // �α� �ý��� �ʱ�ȭ ���� �� ���α׷� ����
    }
    // --- ���� ������ ������ ���� ---
    if (argc < 2) {
        print_usage();
        return 1;
    }

    MYSQL* conn = db_connect();
    if (conn == NULL) {
        return 1; // DB ���� ���� �� ����
    }

    // ù ��° ����(���)�� ���� �б�
    if (strcmp(argv[1], "--borrow") == 0) {
        if (argc != 6 || strcmp(argv[2], "--user_id") != 0 || strcmp(argv[4], "--book_id") != 0) {
            fprintf(stderr, "--borrow�� ���� �߸��� ���ڰ� ���޵Ǿ����ϴ�.\n");
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
            handle_return(conn, argv[3]); // handle_return �Լ� ȣ��
        }
       
    }
    else {
        fprintf(stderr, "�߸��� ��ɾ� �Դϴ�. '%s'\n", argv[1]);
        print_usage();
    }

    mysql_close(conn);
    return 0;
}










