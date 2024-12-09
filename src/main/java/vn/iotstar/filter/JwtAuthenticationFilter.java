package vn.iotstar.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	
	
	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws ServletException, IOException {
	    logger.info("JwtAuthenticationFilter: Entering doFilterInternal");
	    String authHeader = request.getHeader("Authorization");
	    logger.info("JwtAuthenticationFilter: Authorization Header: {}", authHeader);
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        logger.warn("JwtAuthenticationFilter: No Bearer token found");
	        chain.doFilter(request, response);
	        return;
	    }
	    String jwt = authHeader.substring(7);
	    try {
	        String userEmail = jwtService.extractUsername(jwt);
	        logger.info("JwtAuthenticationFilter: Extracted username: {}", userEmail);
	        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
	        if (userDetails == null) {
	            logger.error("JwtAuthenticationFilter: userDetails is null for user: {}", userEmail);
	            return;
	        }
	        logger.info("JwtAuthenticationFilter: User details loaded: {}", userDetails);
	        if (jwtService.isTokenValid(jwt, userDetails)) {
	            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                    userDetails, null, userDetails.getAuthorities());
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	            logger.info("JwtAuthenticationFilter: Authentication successful.  Authorities: {}", userDetails.getAuthorities());
	        } else {
	            logger.warn("JwtAuthenticationFilter: Invalid JWT token");
	        }
	    } catch (Exception ex) {
	        logger.error("JwtAuthenticationFilter: Error during authentication: ", ex);
	    }
	    chain.doFilter(request, response);
	}

}
