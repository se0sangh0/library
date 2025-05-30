<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <script>
        let isIdChecked = false; // 아이디 중복 확인 여부 플래그

        function checkId() {
            const usernameInput = document.getElementById('username');
            const username = usernameInput.value;
            const feedback = document.getElementById('id_feedback');

            if (username.length < 4) {
                feedback.innerHTML = '아이디는 4자 이상이어야 합니다.';
                feedback.style.color = 'red';
                return;
            }

            // fetch API를 사용해 서버에 아이디 중복 여부를 비동기적으로 물어봅니다.
            fetch('check_id.php?username=' + username)
                .then(response => response.json())
                .then(data => {
                    if (data.is_duplicate) {
                        feedback.innerHTML = '이미 사용 중인 아이디입니다.';
                        feedback.style.color = 'red';
                        isIdChecked = false;
                    } else {
                        feedback.innerHTML = '사용 가능한 아이디입니다.';
                        feedback.style.color = 'green';
                        isIdChecked = true;
                    }
                    // 중복 확인 후에는 가입 버튼 활성화/비활성화 상태를 업데이트
                    updateSubmitButtonState();
                });
        }

        function onIdInput() {
            // 사용자가 아이디를 다시 입력하면, 중복 확인 상태를 초기화합니다.
            isIdChecked = false;
            document.getElementById('id_feedback').innerHTML = '';
            updateSubmitButtonState();
        }

        function updateSubmitButtonState() {
            const submitButton = document.getElementById('submit_btn');
            if (isIdChecked) {
                submitButton.disabled = false;
                submitButton.style.backgroundColor = ''; // 원래 색으로
                submitButton.style.color = '';
            } else {
                submitButton.disabled = true;
                submitButton.style.backgroundColor = 'grey'; // 회색으로
                submitButton.style.color = 'white';
            }
        }
    </script>
</head>
<body onload="updateSubmitButtonState()">
    <h2>도서관 회원가입</h2>
    <p>환영합니다. 아래 정보를 기재 후 가입 버튼을 눌러주세요.</p>
    <form action="join_process.php" method="POST">
        <p>이름(필수): <input type="text" name="name" required></p>
        <p>전화번호(필수): <input type="text" name="phone" placeholder="010-1234-5678" required></p>
        <p>생년월일(필수): <input type="date" name="birthdate" required></p>
        <p>이메일: <input type="email" name="email"></p>
        <p>사용할 아이디(필수):
            <input type="text" id="username" name="username" oninput="onIdInput()" required>
            <button type="button" onclick="checkId()">중복 확인</button>
            <span id="id_feedback"></span>
        </p>
        <p>사용할 비밀번호(필수): <input type="password" name="password" required></p>
        <p>비밀번호 확인(필수): <input type="password" name="password_confirm" required></p>
        <p><input type="submit" id="submit_btn" value="회원가입"></p>
    </form>
</body>
</html>