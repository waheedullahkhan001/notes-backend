package com.cloud.notesbackend.filters;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cloud.notesbackend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;

@Log4j2
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String jwt = header.replaceFirst("Bearer ", "");

            try {
                DecodedJWT decodedJWT = jwtService.getDecodedToken(jwt);
                String username = decodedJWT.getClaim("username").asString();
                String role = decodedJWT.getClaim("role").asString();

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (TokenExpiredException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                log.debug("Token expired: {}", e.getMessage());
                return;
            } catch (Exception e) {
                log.error("Error in AuthorizationFilter: {}", e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        super.doFilterInternal(request, response, chain);
    }
}
