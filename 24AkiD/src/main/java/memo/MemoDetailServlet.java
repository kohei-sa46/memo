package memo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemoDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        
        MemoDAO memoDAO = new MemoDAO();
        String memoIds = request.getParameter("memoId");

        if (memoIds != null && !memoIds.isEmpty()) {
            try {
                int memoId = Integer.parseInt(memoIds);
                Memo memo = memoDAO.getMemoId(memoId);
                request.setAttribute("memo", memo);
                request.getRequestDispatcher("memoDetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
            	e.printStackTrace();
            	response.sendRedirect("error.jsp");
            }
        } else {
        	response.sendRedirect("error.jsp");
        }
    }
}
