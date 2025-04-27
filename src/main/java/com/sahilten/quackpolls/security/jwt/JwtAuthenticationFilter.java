package com.sahilten.quackpolls.security.jwt;

import com.sahilten.quackpolls.security.user.QuackpollUserDetails;
import com.sahilten.quackpolls.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // Skip token validation for public auth routes
        if (path.equals("/v1/auth/login") || path.equals("/v1/auth/register") || path.equals("/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);

            // If no token found, silently proceed
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = authenticationService.validateAndExtract(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (userDetails instanceof QuackpollUserDetails) {
                request.setAttribute("userId",
                        ((QuackpollUserDetails) userDetails).getUserId());
            }
        } catch (Exception ex) {
            log.warn("Received invalid auth token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired access token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (path.equals("/v1/auth/refresh")) {
            return extractCookie(request, "refresh_token");
        } else {
            return extractCookie(request, "access_token");
        }
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
