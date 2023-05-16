import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;

@WebFilter(value = "/time/*")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (checkV(req)){
            chain.doFilter(req, res);
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid timezone");
        }

    }
    protected boolean checkV(HttpServletRequest req) {
        try {
            new TimeServlet().timeChecker(req);
            return true;
        } catch (DateTimeException e){
            return  false;
        }
    }
}
