package summer.rest.worldcupfantasy.interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;
import summer.rest.worldcupfantasy.services.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {


    TokenService tokenService;

    public JwtInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ApiErrorResponse {
        try {
            return tokenService.validateJwtToken(request.getHeader("Authorization").split(" ")[1]);
        } catch (Exception e) {
            throw new ApiErrorResponse(HttpStatus.UNAUTHORIZED, "Authorization is required !");
        }
    }
}
