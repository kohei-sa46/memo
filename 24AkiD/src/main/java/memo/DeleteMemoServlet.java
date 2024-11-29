package memo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteMemoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String memoIdStr = request.getParameter("memoId");
        if (memoIdStr != null && !memoIdStr.isEmpty()) {
            try {
                int memoId = Integer.parseInt(memoIdStr);
                MemoDAO memoDAO = new MemoDAO();
                memoDAO.deleteMemo(memoId); 
                response.setStatus(HttpServletResponse.SC_OK); 
            } catch (NumberFormatException e) {
                e.printStackTrace();  
            	response.sendRedirect("error.jsp");
                }
        } else {
        	response.sendRedirect("error.jsp");
        }
    }
}
