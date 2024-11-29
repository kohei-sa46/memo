package memo;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthenticateFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {}

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession(false); 
        
        String userId = (String) (session != null ? session.getAttribute("user_id") : null);

        if (userId == null) {
            String servletPath = httpReq.getServletPath();
            httpReq.setAttribute("target", servletPath);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/register.jsp"); 
            dispatcher.forward(req, res);
        } else {
            chain.doFilter(req, res);
        }
    }
}
