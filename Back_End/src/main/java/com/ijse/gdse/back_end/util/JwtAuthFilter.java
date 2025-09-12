package com.ijse.gdse.back_end.util;

import com.ijse.gdse.back_end.entity.User;
import com.ijse.gdse.back_end.entity.Volunteer;
import com.ijse.gdse.back_end.repository.UserRepository;
import com.ijse.gdse.back_end.repository.VolunteerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String email = jwtUtil.extractUsername(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Object principal = null;
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (userRepository.findByEmail(email).isPresent()) {
                User user = userRepository.findByEmail(email).get();
                principal = user.getEmail();  // <-- FIX: email use කරන්න
                authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
            } else if (volunteerRepository.findByEmail(email).isPresent()) {
                Volunteer volunteer = volunteerRepository.findByEmail(email).get();
                principal = volunteer.getEmail();  // <-- FIX: email use කරන්න
                authorities.add(new SimpleGrantedAuthority("ROLE_VOLUNTEER"));
            }


            if (principal != null && jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}