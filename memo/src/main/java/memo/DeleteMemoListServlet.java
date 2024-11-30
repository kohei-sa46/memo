package memo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteMemoListServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        String[] memoIds = request.getParameterValues("memoIds");

        if (memoIds != null && memoIds.length > 0) {
            MemoDAO memoDAO = new MemoDAO();
            try {
                for (String memoId : memoIds) {
                    memoDAO.deleteMemo(Integer.parseInt(memoId));
                }
                response.sendRedirect("memoList"); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("error.jsp");
        }
    }
}
