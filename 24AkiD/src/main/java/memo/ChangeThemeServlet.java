package memo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeThemeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("user_id");

        if (userId != null) {
            UserDAO userDAO = new UserDAO();
            try {
                // セッションから現在のテーマを取得
                String currentTheme = (String) session.getAttribute("theme_mode");
                // 初期値を設定
                if (currentTheme == null) {
                    currentTheme = "light"; 
                }
                System.out.println("現在のテーマ: " + currentTheme);

                // テーマを切り替える
                String newTheme = "dark".equals(currentTheme) ? "light" : "dark";

                // テーマを更新
                userDAO.updateTheme(userId, newTheme);
                System.out.println("新しいテーマ: " + newTheme);

                // 新しいテーマをセッションに保存
                session.setAttribute("theme_mode", newTheme);

                // 正常なレスポンスを返す
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(newTheme); // 新しいテーマをレスポンスに返す
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("エラー: " + e.getMessage()); // エラーメッセージを返す
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
