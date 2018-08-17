package com.tsm.template.security;


import com.tsm.template.model.User;
import com.tsm.template.service.JwtTokenUtil;
import com.tsm.template.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String authToken = req.getHeader(SecurityConstants.HEADER_STRING);
        String userEmail = null;

        if (StringUtils.isNotBlank(authToken)) {
            try {
                userEmail = jwtTokenUtil.getUserEmailFromToken(authToken);
            } catch (IllegalArgumentException e) {
                log.error("An error occured during getting username from token.", e);
            } catch (ExpiredJwtException e) {
                log.warn("The token is expired and not valid anymore.", e);
            } catch (MalformedJwtException e) {
                log.warn(String.format("The token is malformed [%s].", authToken), e);
            } catch (Exception e) {
                log.warn(String.format("The token is invalid [%s].", authToken), e);
            }
        } else {
            log.warn("Couldn't find bearer string, will ignore the header.");
        }
        if (!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userService.findByEmail(userEmail);

            if (jwtTokenUtil.validateToken(authToken, user.getEmail())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null, Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                log.info("Authenticated user [{}] setting security context.", userEmail);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(req, res);
    }

}
