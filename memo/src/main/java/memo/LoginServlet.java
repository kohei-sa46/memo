package memo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDao;
    @Override
    public void init() throws ServletException {
        userDao = new UserDAO();  
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");

        try {
            boolean isValidUser = userDao.validateUser(userId, password);
            if (isValidUser) {
                // ログイン成功時に新しいセッションを開始する（既存のセッションがあれば再利用）
                HttpSession session = request.getSession(true); 
                session.setAttribute("user_id", userId);
                
                // ユーザーのテーマをデータベースから取得してセッションに保存
                String themeMode = userDao.getCurrentTheme(userId);
                session.setAttribute("theme_mode", themeMode); // デフォルトテーマをセッションにセット

                // メモ一覧ページにリダイレクト
                response.sendRedirect("memoList"); 
            } else {
                // ログイン失敗時、登録ページにフォワード
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); 
        }
    }

}