package memo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MemoListServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    response.setContentType("text/html; charset=UTF-8");
	    MemoDAO memoDAO = new MemoDAO();
	    HttpSession session = request.getSession();

	    if (session == null || session.getAttribute("user_id") == null) {
	        response.sendRedirect("memo");
	        return;
	    }

	    String userId = (String) session.getAttribute("user_id");

	    // テーマ設定をデータベースから取得
	    String theme = memoDAO.getUserTheme(userId);  
	    if (theme == null) {
	        theme = "light";  // デフォルトのテーマを指定
	    }
	    request.setAttribute("theme_mode", theme);

	    String keyword = request.getParameter("keyword");
	    int page = 1;
	    int memosPerPage = 20;

	    if (request.getParameter("page") != null) {
	        page = Integer.parseInt(request.getParameter("page"));
	    }

	    int offset = (page - 1) * memosPerPage;

	    List<Memo> memoList;
	    int totalMemos;
	    boolean hasSearchResults = false;

	    if (keyword != null && !keyword.isEmpty()) {
	        // 検索結果の場合
	        totalMemos = memoDAO.searchMemoCount(userId, keyword);  // 検索結果の総件数を取得
	        memoList = memoDAO.searchMemos(userId, keyword, offset, memosPerPage);
	        hasSearchResults = (memoList != null && !memoList.isEmpty());
	    } else {
	        // 通常のメモ一覧表示
	        totalMemos = memoDAO.getTotalMemoCount(userId);
	        memoList = memoDAO.getMemoList(userId, offset, memosPerPage);
	    }

	    // ページ数の計算
	    int totalPages = (int) Math.ceil((double) totalMemos / memosPerPage);

	    // JSPにデータを渡す
	    request.setAttribute("memoList", memoList);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalPages", totalPages);
	    request.setAttribute("totalMemos", totalMemos);  // 総メモ数
	    request.setAttribute("hasSearchResults", hasSearchResults); // 検索結果の有無

	    request.getRequestDispatcher("memoList.jsp").forward(request, response);
	}


}
