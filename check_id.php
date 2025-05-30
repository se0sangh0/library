<?php
include 'config.php'; // DB 연결

$username = $_GET['username'];
$response = ['is_duplicate' => false];

// 보안을 위해 PreparedStatement 사용
$stmt = $conn->prepare("SELECT username FROM users WHERE username = ?");
$stmt->bind_param("s", $username);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $response['is_duplicate'] = true;
}

$stmt->close();
$conn->close();

header('Content-Type: application/json');
echo json_encode($response);
?>