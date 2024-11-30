<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>ERROR</title>
    <link rel="stylesheet" href="css/error.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            
        }
        

        img {
        
            width: 600px; 
            height: 600px; 
            object-fit: cover; 
        }
         body.dark-mode {
            background-color:black;
        }

        .buttonBack {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
        }

        .buttonBack:hover {
            background-color: rgb(212, 212, 212);
        }

        .buttonBack i {
            color: black;
            font-size: 40px;
        }
        .dark-mode .buttonBack{
        	background-color: #1a1a1a;
    		background: linear-gradient(to right, #1a1a1a, #1a1a1a);
    		color: white;
    		border: 2px solid gold;
        }
        .dark-mode .buttonBack i {
    		color: white;
		}
		.dark-mode .buttonBack i:hover{
		color:gold;
		}
        
    </style>

    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function() {
            // サーバーからセッションで取得したテーマモードをsessionStorageに保存
            const serverTheme = "<%= session.getAttribute("theme_mode") != null ? session.getAttribute("theme_mode") : "light" %>";
            if (!sessionStorage.getItem("theme_mode")) {
                sessionStorage.setItem("theme_mode", serverTheme);
            }

            // テーマモードに応じた画像を設定
            const savedTheme = sessionStorage.getItem("theme_mode");
            const errorImage = document.getElementById("errorImage");
            
            if (savedTheme === 'dark') {
                errorImage.src = "image/darkerror.jpeg";
                document.body.classList.add('dark-mode');
            } else {
                errorImage.src = "image/error.jpeg";
            }
        });
    </script>
</head>

<body>
    <img id="errorImage" src="image/error.jpeg" alt="Error Image">
    <button class="buttonBack" title="戻る"><a href="memoList"><i class='bx bxs-share'></i></a></button>
</body>
</html>
