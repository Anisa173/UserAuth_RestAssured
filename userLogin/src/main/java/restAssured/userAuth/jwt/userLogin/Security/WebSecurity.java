package restAssured.userAuth.jwt.userLogin.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import restAssured.userAuth.jwt.userLogin.Service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private BCryptPasswordEncoder passwordEncoder;
    private UserService uService;
    private Environment webEnvironment;

    public WebSecurity(BCryptPasswordEncoder passwordEncoder, UserService uService, Environment webEnvironment) {
        this.passwordEncoder = passwordEncoder;
        this.uService = uService;
        this.webEnvironment = webEnvironment;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(uService).passwordEncoder(passwordEncoder);

        AuthenticationManager authManager = authManagerBuilder.build();

        httpSecurity.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->auth
                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/{userId}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/all").permitAll()
                                .anyRequest().authenticated())
                .addFilter(new AuthenticationFilter((AuthenticationManager) uService, (UserService) webEnvironment, (Environment) authManager))
                .addFilter(new AuthorizationFilter(authManager, webEnvironment))
                .authenticationManager(authManager)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                 httpSecurity.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()));

        return httpSecurity.build();


    }


}
