import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//http://localhost:8080/jd9-md9-hw/time?timezone=12

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private List list = new ArrayList();
    private String lastTimezone = "";

    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/Users/art_of_d/Java/jd9-md9-hw/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        HttpSession session = req.getSession(true);
        session.setMaxInactiveInterval(3 * 60);
        if (session.isNew()){
            list.add(session.getId());
        }

        Context simpleContext = new Context(req.getLocale(), Map.of("queryParams", timeChecker(req)));

        engine.process("test", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }

    protected String timeChecker(HttpServletRequest req){
        String timeFormate = "дата: dd.MM.yyyy, час: HH:mm:ss, зона: z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormate);
        String currentTime;

        if ( (!lastTimezone.isEmpty()) && (checkCookies(req.getCookies())) ){
            currentTime = formatter.format(ZonedDateTime.now(ZoneId.of(lastTimezone)));
        }
        else if (req.getParameterMap().containsKey("timezone")) {
            lastTimezone = "UTC+" + req.getParameter("timezone");
            currentTime = formatter.format(ZonedDateTime.now
                    (ZoneId.of("UTC+" + req.getParameter("timezone"))));
        }
        else {
            currentTime = formatter.format(ZonedDateTime.now(ZoneId.of("UTC")));
        }
        return currentTime;
    }

    private boolean checkCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (list.contains(cookie.getValue())) {
                return true;
            }
        }
        return false;
    }

}
