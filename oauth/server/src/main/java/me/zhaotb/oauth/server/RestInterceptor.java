package me.zhaotb.oauth.server;

import me.zhaotb.oauth.server.service.AccessHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessHandlerService accessHandlerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("authorization");
        User user = accessHandlerService.accessUser(authorization);
        String uri = request.getRequestURI();
        if (user != null) {
            Set<String> scopes = user.getScopes();
            if (scopes.contains(uri)) {
                request.setAttribute("user", user);
                return true;
            }
        }
        request.getRequestDispatcher("error/401?uri=" + uri).forward(request, response);
        return true;
    }
}
