package com.library.library.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            FilterChain filterChain) throws IOException, ServletException {
        final String authHeader;
        final String jwtToken;
        final String userEmail;

        System.out.println(request.getRequestURI());

        authHeader = request.getHeader("Authorization");

        //Ensure header is not null and use JWT token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        System.out.println(jwtToken);
        userEmail = jwtService.extractUsername(jwtToken);
        System.out.println(userEmail);

        // If token contained a valid username and If the user is not already authenticated
        if (
                userEmail != null && SecurityContextHolder
                .getContext().getAuthentication() == null) {
            // Loads the full user details
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            //check token is valid or not. validate JWT
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                //Creates a Spring Security Authentication object using the validated user.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                // Adds details like client IP, session ID, etc., to the auth object.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Sets the user as authenticated in the Spring Security context, so now the request is treated as an authenticated request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Proceeds with the filter chain (i.e., lets the request continue to your controllers).
        filterChain.doFilter(request, response);
    }
}



