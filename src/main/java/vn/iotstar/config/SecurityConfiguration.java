package vn.iotstar.config;

//Bước 7: Khởi tạo một class SecurityConfig
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.iotstar.filter.JwtAuthenticationFilter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    public SecurityConfiguration(AuthenticationProvider authenticationProvider,
                                  JwtAuthenticationFilter jwtAuthenticationFilter,
                                  UserDetailsService userDetailsService) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        logger.info("SecurityConfiguration: filterChain called");
        return httpSecurity
                .csrf(csrf -> csrf.disable()) 
                .authorizeHttpRequests(auth -> auth
                       /* .requestMatchers("/auth/**").permitAll() 
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/static/**").permitAll() //This line already covers it
                        .requestMatchers("/static/fe/**").permitAll() //Redundant, but explicit
                        .requestMatchers("/static/js/**").permitAll()  //Redundant, but explicit
                        .requestMatchers("/static/assets/css/**").permitAll() //Redundant, but explicit
                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll() //Redundant
                        .requestMatchers("/user/**").hasRole("USER") 
                        .requestMatchers("/admin").hasRole("ADMIN") */
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
                .authenticationProvider(authenticationProvider) 
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) 
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
                .build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8090")); // Domain được phép
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
