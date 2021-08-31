package uz.spring.appownjwtpractice.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.spring.appownjwtpractice.entity.User;
import uz.spring.appownjwtpractice.service.RegisterService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    final JwtProvider jwtProvider;
    final RegisterService registerService;

    public JwtFilter(JwtProvider jwtProvider, RegisterService userService) {
        this.jwtProvider = jwtProvider;
        this.registerService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            String tokenWithoutBearer = token.substring(7);
            if (jwtProvider.validateToken(tokenWithoutBearer)) {
                String email = jwtProvider.userNameFromToken(tokenWithoutBearer);
                if (email != null) {
                    UserDetails userDetails = registerService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
