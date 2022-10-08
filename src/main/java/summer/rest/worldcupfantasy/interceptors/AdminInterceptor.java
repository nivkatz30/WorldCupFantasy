package summer.rest.worldcupfantasy.interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import summer.rest.worldcupfantasy.entities.User;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.models.UserRole;
import summer.rest.worldcupfantasy.repos.UserRepo;
import summer.rest.worldcupfantasy.services.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    TokenService tokenService;
    UserRepo userRepo;

    public AdminInterceptor(TokenService tokenService, UserRepo userRepo) {
        this.tokenService = tokenService;
        this.userRepo = userRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ApiErrorResponse {
        try {
            Long userId = this.tokenService.getUserIdFromToken(request.getHeader("Authorization").split(" ")[1]);
            User user = userRepo.getOrThrowById(userId);

            if (user.getRole() != UserRole.ADMIN) {
                throw new Exception();
            }

            return true;
        } catch (Exception e) {
            throw new ApiErrorResponse(HttpStatus.UNAUTHORIZED, "You are not authorized for these type of request !");
        }
    }
}
