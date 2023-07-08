package com.javatechie.springSecurityLatest.filter;

import com.javatechie.springSecurityLatest.config.UserInfoUserDetailsService;
import com.javatechie.springSecurityLatest.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter
{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        //Bearer dnisbvivbukvjhsbivhvskiu
        String token=null;
        String username=null;
        String password=null;
        if(authHeader!=null &&authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7);
            username=jwtService.extractUsername(token);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
            {
                UserDetails userDetails =userDetailsService.loadUserByUsername(username);

                if(jwtService.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

            }
                filterChain.doFilter(request,response);

        }

    }
}
