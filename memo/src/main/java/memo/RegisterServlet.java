package memo;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDao;
    @Override
    public void init() throws ServletException {
        userDao = new UserDAO();  
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 登録ページ（register.jsp）へフォワードする
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // ユーザーIDとパスワードの検証
        if (!username.matches("[A-Za-z0-9]+") || !password.matches("[A-Za-z0-9]+")) {
            request.setAttribute("message", "ユーザーIDとパスワードは半角アルファベットと半角数字のみ使用できます");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            // ユーザーをデータベースに挿入
            userDao.insertUser(username, password);
            // ユーザー登録成功時
            request.setAttribute("message", "ユーザー登録に成功しました。");
            //request.setAttribute("closeModal", "true");  // 成功時はモーダルを閉じる
        } catch (SQLIntegrityConstraintViolationException e) {
            // 一意制約違反（重複エラー）の場合
            e.printStackTrace();
            request.setAttribute("message", "入力されたIDは既に使用されています");
        } catch (SQLException e) {
            // その他のSQLエラーの場合
            e.printStackTrace();
            request.setAttribute("message", "登録に失敗しました");
        }

        // JSPへforwardしてメッセージとモーダルの情報を伝える
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

}