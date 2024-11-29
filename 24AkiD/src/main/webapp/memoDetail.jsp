<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="memo.Memo"%>
<%@ page import="memo.Utils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html>
<head>
    <title>memo</title>
    <link rel="stylesheet" href="css/memoDetail.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function() {
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
          // 編集モーダルを表示
    function showEditModal(content, memoId, event) {
        event.stopPropagation(); // 親要素へのクリックイベント伝播を防ぐ
        var modal = document.getElementById("editModal");
        var textarea = document.getElementById("memoContent");

        if (!textarea) {
            console.error("Textarea element not found");
            return;
        }

        // HTML contentの <br> タグを改行コードに変換して textarea にセット
        textarea.value = content.replace(/<br\s*\/?>/gi, '\n');
        document.getElementById("editMemoId").value = memoId; // メモIDも保存
        modal.style.display = "block";
    }
    function updateMemo() {
        const xhr = new XMLHttpRequest();
        
        // メモ内容を取得
        let content = document.getElementById("memoContent").value;

        // 改行を \n にエスケープ（バックスラッシュはそのまま）
        content = content.replace(/\n/g, '\\n');

        const memoId = document.getElementById("editMemoId").value;

        xhr.open('POST', 'UpdateMemoServlet', true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.status === 'Success') {
                        closeEditModal();
                        window.location.reload();
                    }
                } catch (e) {
                    console.error("JSON parse error:", e);
                }
            }
        };

        // JSONデータを準備
        const requestData = JSON.stringify({
            memoId: memoId,
            content: content
        });

        xhr.send(requestData);
    }

        
     
    // モーダルを閉じる
    function closeEditModal() {
        document.getElementById("editModal").style.display = "none";
    }

    // 削除モーダルの表示と処理
    function showDeleteModal(memoId) {
        var modal = document.getElementById("deleteModal");
        modal.style.display = "block";

        document.getElementById("confirmDelete").onclick = function() {
            deleteMemo(memoId);
            modal.style.display = "none";
        };

        document.getElementById("cancelDelete").onclick = function() {
            modal.style.display = "none";
        };
    }

    function deleteMemo(memoId) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'DeleteMemoServlet', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                window.location.href = 'memoList'; // メモ一覧ページにリダイレクト
            }
        };
        xhr.send('memoId=' + memoId);
    }
    </script>
</head>

<body class="<%= session.getAttribute("theme_mode") != null ? session.getAttribute("theme_mode") : "light" %>">
    <div class="list">
        <%
            Memo memo = (Memo) request.getAttribute("memo");
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd(E) HH:mm:ss");
        %>
        <p><strong>メモ作成日時: </strong> <%= date.format(memo.getCreatedAt()) %>
        <% if (memo.getUpdatedAt() != null) { %>
            <strong>最終更新日時: </strong> <span id="updatedAt"><%= date.format(memo.getUpdatedAt()) %></span></p>
        <% } %>
        
        <div id="memoContentDisplay">
            <%= memo.getContent() %>
        </div>

        <!-- 削除モーダル -->
        <div id="deleteModal" class="modal">
            <div class="modal-content">
                <p>メモを削除しますか？</p>
                <button id="confirmDelete">DELETE</button>
                <button id="cancelDelete">CANCEL</button>
            </div>
        </div>

        <!-- 編集モーダル -->
        <div id="editModal" class="modal">
            <div class="modal-content">
                <textarea id="memoContent" rows="4" cols="30" maxlength="2000"></textarea><br>
                <input type="hidden" id="editMemoId" /> 
                <button onclick="updateMemo()">UPDATE</button>
                <button onclick="closeEditModal()">CANCEL</button>
            </div>
        </div>

        <!-- ボタン -->
        <div class="button">
			<button class="buttonBack" title="戻る"><a href="memoList"><i class='bx bxs-share'></i></a></button>
            <button class="button-del" onclick="showDeleteModal(<%= memo.getMemoId() %>)" title="削除"><i class='bx bx-trash'></i></button>
			<button class="edit-button" onclick="showEditModal('<%= memo.getContent().replace("\\", "\\\\").replace("\n", "<br>").replace("\r", "\\r").replace("'", "\\'").replace("\"", "\\\"") %>', <%= memo.getMemoId() %>, event)" title="編集"><i class='bx bx-edit-alt'></i></button>

        </div>
    </div>
</body>
</html>
