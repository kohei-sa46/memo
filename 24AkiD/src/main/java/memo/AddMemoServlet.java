package memo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddMemoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String content = request.getParameter("content");       
        String userId = (String) request.getSession().getAttribute("user_id");
        
        if (content != null && !content.isEmpty()) {
            MemoDAO memoDAO = new MemoDAO();
            memoDAO.addMemo(content, userId);
        }
        response.sendRedirect("memoList");
    }
}
