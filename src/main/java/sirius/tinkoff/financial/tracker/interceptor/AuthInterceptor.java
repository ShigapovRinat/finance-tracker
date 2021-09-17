package sirius.tinkoff.financial.tracker.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sirius.tinkoff.financial.tracker.exception.UnableToFindUserException;
import sirius.tinkoff.financial.tracker.model.Session;
import sirius.tinkoff.financial.tracker.service.CreatorUserService;
import sirius.tinkoff.financial.tracker.service.UserService;
import sirius.tinkoff.financial.tracker.util.DefaultUserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final CreatorUserService creatorUserService;
    private final Session session;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String login = request.getHeader("login");
        try {
            session.setUserEntity(userService.getUserEntityByLogin(login));
        } catch (UnableToFindUserException e) {
            session.setUserEntity(
                    creatorUserService.createUser(DefaultUserEntity.get()
                            .setLogin(login)
                            .setName("Unknown")
                            .setSurname("User")
                            .setPhoneNumber(123L)));
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }
}
