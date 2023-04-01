package com.app.security;



import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtTokenGeneratorFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
        	
        	System.out.println("authenticationnnn "+authentication);
        	
        	
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes());
            
            
            String jwt = Jwts.builder()
            		.setIssuer("sagar")
            		.setSubject("Token")
                    .claim("username", authentication.getName())
                    .claim("role",getRole(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime()+ 30000000))
                    .signWith(key).compact();
            
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
 
        
        }

        filterChain.doFilter(request, response);
    	
		
		
	}
	
	
	  private String getRole(Collection<? extends GrantedAuthority> collection) {
	        
	    String role="";
		  for(GrantedAuthority ga:collection) {
			  role= ga.getAuthority();
		  }
	    
		  return role;
	    }
		
	
		
		
		
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	
		 return !request.getServletPath().matches("/public/signIn");	
	}
	

}

