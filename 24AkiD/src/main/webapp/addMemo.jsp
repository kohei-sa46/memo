<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add memo</title>
    <script type="text/javascript">
        function closePopup() {
            window.opener.location.reload(); // 親ウィンドウをリロード
            window.close(); // ポップアップを閉じる
        }
    </script>
</head>
<body>
    <form action="AddMemoServlet" method="post" onsubmit="closePopup()">
        <textarea name="content" rows="5" cols="40" maxlength="2000"></textarea><br>
        <input type="submit" value="Add">
    </form>
</body>
</html>
