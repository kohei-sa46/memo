<%@ page session="true" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="memo.Memo"%>
<%@ page import="memo.MemoDAO"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.net.URLEncoder" %>

<html>
<head>
<title>memo</title>
<link rel="stylesheet" href="css/memoList.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<script type="text/javascript">
    function openModal() {
        document.getElementById('overlay').style.display = 'block';
        document.getElementById('modal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('overlay').style.display = 'none';
        document.getElementById('modal').style.display = 'none';
    }

    function toggleEditMode() {
        var editControls = document.getElementById('editControls');
        var checkboxes = document.querySelectorAll('.memo-checkbox');

        if (editControls.style.display === 'none' || editControls.style.display === '') {
            editControls.style.display = 'block';
            checkboxes.forEach(function(checkbox) {
                checkbox.style.display = 'inline-block';
            });
        } else {
            editControls.style.display = 'none';
            checkboxes.forEach(function(checkbox) {
                checkbox.style.display = 'none';
            });
        }
    }
    // チェックボックスの選択数をカウントする機能
    document.addEventListener('DOMContentLoaded', function() {
        var checkboxes = document.querySelectorAll('.memo-checkbox');
        var selectedCountDisplay = document.getElementById('selectedCount');

        checkboxes.forEach(function(checkbox) {
            checkbox.addEventListener('change', function() {
                var selectedCount = document.querySelectorAll('.memo-checkbox:checked').length;
                selectedCountDisplay.textContent = selectedCount;
            });
        });

        // サーバーからセッションで取得したテーマモードをsessionStorageに保存
        const serverTheme = "<%= session.getAttribute("theme_mode") != null ? session.getAttribute("theme_mode") : "light" %>";
        if (!sessionStorage.getItem("theme_mode")) {
            sessionStorage.setItem("theme_mode", serverTheme);
        }

        // ページ読み込み時にテーマを適用
        const savedTheme = sessionStorage.getItem("theme_mode");
        if (savedTheme) {
            document.body.classList.toggle('dark-mode', savedTheme === 'dark');
        }
    });

    function toggleTheme() {
        const currentTheme = sessionStorage.getItem("theme_mode") || "light";
        const newTheme = currentTheme === "light" ? "dark" : "light";
        document.body.classList.toggle('dark-mode', newTheme === "dark");

        document.querySelectorAll('.list, .searchList, .search-input, .search-button, .logout-form, .edit-button, .add-button, #modal, #overlay')
            .forEach(element => element.classList.toggle('dark-mode', newTheme === "dark"));

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "changeTheme", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send("theme_mode=" + newTheme);

        xhr.onload = function() {
            if (xhr.status === 200) {
                sessionStorage.setItem("theme_mode", newTheme);
            } else {
                alert("テーマの変更に失敗しました。");
            }
        };
    }

    function clearSessionStorage() {
        sessionStorage.clear();
    }

    function scrollToTop() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    function scrollToBottom() {
        window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
    }
</script>
</head>

<body class="<%= (String) session.getAttribute("theme_mode") != null ? (String) session.getAttribute("theme_mode") : "light" %>">

	<div class="theme-toggle">
		<label class="switch"> 
            <input type="checkbox" onchange="toggleTheme()"  <%= (session.getAttribute("theme_mode") != null && session.getAttribute("theme_mode").equals("dark")) ? "checked" : "" %>>
			<span class="slider"></span>
		</label>
	</div>

	
	<!-- topとbottomボタン -->
	<div class=buttonTB>
	<%
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.isEmpty()) {
        %>
		<form class="button-BL" action="${pageContext.request.contextPath}/memoList" method="get" title="戻る">
			<button class="buttonBack"><i class='bx bxs-share'></i></button>
		</form>
		<%
        }
        %>
			<button class="buttonTop" onclick="scrollToTop()" title="上へ"><i class='bx bx-vertical-top'></i></button>
			
			
	    	<button class="buttonBot" onclick="scrollToBottom()" title="下へ"><i class='bx bx-vertical-bottom'></i></button>
		</div>
		
	<div class="list <%= (String) session.getAttribute("theme_mode") != null ? (String) session.getAttribute("theme_mode") : "light" %>">
		<div class="searchList <%= (String) session.getAttribute("theme_mode") != null ? (String) session.getAttribute("theme_mode") : "light" %>">
			<div class="user">
				<h2 class="user-name">
					<i class='bx bx-user-circle'></i><%=(String) session.getAttribute("user_id")%></h2>
				<form action="logout" method="get" onsubmit="clearSessionStorage()" title="ログアウト">
					<button type="submit" class="logout-form">
						<i class='bx bx-log-out'></i>
					</button>
				</form>
			</div>
			<form action="${pageContext.request.contextPath}/memoList" method="get" class="search-form">
				<div class="search-container">
					<input type="text" name="keyword" placeholder="Search memos"
						value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword").trim() : ""%>"
						class="search-input" >
					<button type="submit" class="search-button" title="検索">
						<i class='bx bx-search-alt-2'></i>
					</button>
				</div>
			</form>
		</div>



		<form id="memoForm" action="DeleteMemoListServlet" method="post">
			<div id="editControls" style="display: none;">
				<button class="button-del" type="submit" value="DELETE" title="削除">
					<i class='bx bx-check-circle'></i>
				</button>
								一括削除:<span id="selectedCount">0</span>件選択
			</div>

			<table>
			
					<!-- 検索結果件数をメモ表示テーブルの上部に表示 -->
		<%
        int totalMemos = (Integer) request.getAttribute("totalMemos");
        if (keyword != null && !keyword.isEmpty()) {
        %>
		<div class="search-result-count">
			<%= totalMemos %> 件のメモが見つかりました。
		</div>
		<%
        }
        %>
				<%
                List<Memo> memoList = (List<Memo>) request.getAttribute("memoList");
                if (memoList != null && !memoList.isEmpty()) {
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss");
                    for (Memo memo : memoList) {
                        String content = memo.getShortContent();
                        if (keyword != null && !keyword.isEmpty()) {
                            content = memo.getShortContent();
                         // ハイライトの修正
                            content = content.replaceAll("(?i)(" + keyword + ")", "<span class=\"highlight\">$1</span>");
                        }
                %>
				<tr>
					<td class="date"><%=date.format(memo.getLatestAt())%></td>
				</tr>
				<tr>
					<td class="memo"><a href="memoDetail?memoId=<%=memo.getMemoId()%>"><%=content%></a></td>
					<td><input type="checkbox" class="memo-checkbox" name="memoIds" value="<%=memo.getMemoId()%>"></td>
				</tr>
				<%
                    }
                } 
                %> 
			</table>
		</form>

		<%
        int currentPage = (Integer) request.getAttribute("currentPage");
        int totalPages = (Integer) request.getAttribute("totalPages");

        if (totalMemos == 0) {
            currentPage = 1;
            totalPages = 1;
        }
        %>

		<div id="overlay"></div>
		<div id="modal">
			<button id="closeButton" onclick="closeModal()" title="閉じる">
				<i class='bx bx-x'></i>
			</button>
			<form action="AddMemoServlet" method="post" onsubmit="closeModal()">
				<textarea name="content" rows="5" cols="40" maxlength="2000"></textarea>
				<br> <input type="submit" value="ADD" style="width: 100%; padding: 10px; font-size: 16px;" >
			</form>
		</div>

		<div class="button">
			<div class="memoken">


<div class="buttonpage">
    <% if (currentPage > 1) { %>
        <a href="memoList?page=<%=currentPage - 1%><% if (keyword != null && !keyword.isEmpty()) { %>&keyword=<%= URLEncoder.encode(keyword, "UTF-8") %><% } %>" title="前へ">
            <i class='bx bxs-caret-left-circle'></i> 
        </a>
    <% } else { %>
        <a><i class='bx bxs-caret-left-circle'></i></a>
    <% } %>

    <span class="current-page"><%=currentPage%></span> | <span class="total-pages"><%=totalPages%></span>

    <% if (currentPage < totalPages) { %>
        <a href="memoList?page=<%=currentPage + 1%><% if (keyword != null && !keyword.isEmpty()) { %>&keyword=<%= URLEncoder.encode(keyword, "UTF-8") %><% } %>" title="次へ">
            <i class='bx bxs-caret-right-circle'></i>
        </a>
    <% } else { %>
        <a><i class='bx bxs-caret-right-circle'></i></a>
    <% } %>
</div>

				<!-- 合計メモ数を表示 -->
				<span class="totalmemo"><%=totalMemos%>memos</span>
			</div>

			<div class="buttonAE">
				<!-- 編集ボタン -->
				<button onclick="toggleEditMode()" class="edit-button" title="編集">
					<i class='bx bxs-trash'></i>
				</button>
				<!-- メモ追加ボタン -->
				<button onclick="openModal()" class="add-button" title="追加">
					<i class='bx bx-message-square-add'></i>
				</button>
			</div>
		</div>
	</div>
</body>
</html>