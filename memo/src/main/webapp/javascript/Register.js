// Webページ上にメッセージを表示する関数（成功時）
function showPageMessage(message) {
    const messageContainer = document.getElementById("messageContainer");
    if (message) {
        messageContainer.innerHTML = message;
    }
}

// エラーメッセージを表示する関数
function showErrorMessage(message) {
    const errorContainer = document.getElementById("errorContainer");
    if (message) {
        errorContainer.innerHTML = message;
    }
}

// 初期ロード時にメッセージを処理
window.onload = function() {
    let message = document.querySelector('meta[name="message"]').content;

    // メッセージが存在する場合に表示
    if (message !== 'null') {
        showPageMessage(message);
    }
}

// 半角アルファベットと半角数字チェック用の正規表現
const alphanumericRegex = /^[A-Za-z0-9]+$/;

// フォーム送信前にアルファベットと数字のチェックを行う関数
function validateForm() {
    const userId = document.getElementById("signupUserId").value;
    const password = document.getElementById("signupPassword").value;

    // ユーザーIDがアルファベットと数字かどうかチェック
    if (!alphanumericRegex.test(userId)) {
        showErrorMessage("UserIDは半角アルファベットと半角数字のみ使用してください");
        return false;  // フォーム送信を中断
    }

    // パスワードがアルファベットと数字かどうかチェック
    if (!alphanumericRegex.test(password)) {
        showErrorMessage("パスワードは半角アルファベットと半角数字のみ使用してください");
        return false;  // フォーム送信を中断
    }

    // 両方とも問題ない場合はフォームを送信
    return true;
}

// ボタンのクリックによるクラス切り替え処理
const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});
