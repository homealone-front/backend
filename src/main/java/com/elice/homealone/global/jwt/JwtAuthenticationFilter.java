package com.elice.homealone.global.jwt;

import com.elice.homealone.global.exception.ErrorCode;
import com.elice.homealone.global.exception.HomealoneException;
import com.elice.homealone.global.redis.RedisUtil;
import com.elice.homealone.module.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisUtil redisUtil;

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, RedisUtil redisUtil, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);
            if (token != null && redisUtil.hasKeyBlackList(token)) {
                throw new HomealoneException(ErrorCode.INVALID_TOKEN);
            }
            // 유효한 token인 경우
            if (token != null && jwtTokenProvider.validateAccessToken(token)) {
                String email = jwtTokenProvider.getEmail(token);
                Member member = (Member) userDetailsService.loadUserByUsername(email);
                if (member != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleTokenException(request, response, e, filterChain);
        } catch (JwtException e) {
            setErrorResponse(response, e, "exception");
        }
    }

    private void setErrorResponse(HttpServletResponse res, Throwable ex, String message) throws IOException {
        String errorMessage = ex.getMessage();
        if(message.equals("EXPIRED_REFRESH_TOKEN")) errorMessage = message;
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final Map<String, Object> body = new HashMap<>();
        body.put("error", "UNAUTHORIZED");
        body.put("message", errorMessage);
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(res.getOutputStream(), body);
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void handleTokenException(HttpServletRequest request, HttpServletResponse response, Exception e, FilterChain filterChain) throws IOException, ServletException {
        String path = request.getRequestURI();
        if (path.equals("/api/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            setErrorResponse(response, e, "EXPIRED_REFRESH_TOKEN");
        }
    }
}

