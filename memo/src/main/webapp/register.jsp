<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta name="message" content="<%= request.getAttribute("message") %>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/Register.css">
    <link rel="stylesheet" href="css/Login.css">
    <title>memo</title>
</head>

<body>

    <div class="container" id="container">
        
        <div class="form-container sign-up">
            <form method="post" action="memo" onsubmit="return validateForm()">
                <h1>Create Account</h1>
                <!-- UserID制限: 半角英数字10文字まで -->
                <input type="text" id="signupUserId" name="username" placeholder="UserID" required maxlength="10" pattern="[A-Za-z0-9]{1,10}" title="UserIDは半角アルファベットと半角数字のみ、最大10文字まで使用できます">
                <!-- Password制限: 半角英数字10文字まで -->
                <input type="password" id="signupPassword" name="password" placeholder="Password" required maxlength="10" pattern="[A-Za-z0-9]{1,10}" title="パスワードは半角アルファベットと半角数字のみ、最大10文字まで使用できます">
                <button type="submit">Sign Up</button>
            </form>
            <div id="modalErrorDisplay"></div>
        </div>

        <div class="form-container sign-in">
            <form method="post" action="login">
                <h1>Sign In</h1>
                <!-- UserID制限: 半角英数字10文字まで -->
                <input type="text" name="user_id" placeholder="UserID" required maxlength="10" pattern="[A-Za-z0-9]{1,10}" title="UserIDは半角英数字のみ、最大10文字まで使用できます">
                <!-- Password制限: 半角英数字10文字まで -->
                <input type="password" name="password" placeholder="Password" required maxlength="10" pattern="[A-Za-z0-9]{1,10}" title="パスワードは半角英数字のみ、最大10文字まで使用できます">
                <button type="submit">Sign In</button>
            </form>
        </div>

        <div class="toggle-container">
            <div class="toggle">
                <div class="toggle-panel toggle-left">
                    <h1>Welcome</h1>
                    <button class="hidden" id="login" >Sign In</button>
                </div>
                <div class="toggle-panel toggle-right">
                    <h1>Hello, Friend!</h1>
                    <button class="hidden" id="register" >Sign Up</button>
                </div>
            </div>
        </div>
	    
    </div>
    <script src="javascript/Register.js"></script>
    <div id="messageContainer" style="color:white;font-weight: bold;font-size:25px;"></div>
    <div id="messageDisplay" style="color:white;font-weight: bold;font-size:25px">
        <% 
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
                out.print(errorMessage);
            }
        %>
    </div>

</body>

</html>
