package restAssured.userAuth.jwt.userLogin.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import restAssured.userAuth.jwt.userLogin.Domain.LoginRequestDto;
import restAssured.userAuth.jwt.userLogin.Entity.User;
import restAssured.userAuth.jwt.userLogin.Service.UserService;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter implements UsernamePasswordAuthentication {
    private final UserService userService;
    private Environment webEnvironment;
    private ObjectMapper objMapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment webEnvironment) {
        super(authenticationManager);
        this.userService = userService;
        this.webEnvironment = webEnvironment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws AuthenticationException {

        try {
            LoginRequestDto loginCredencials = objMapper.readValue(httpRequest.getInputStream(), LoginRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredencials.getEmail(), loginCredencials.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication auth, FilterChain chain) throws IOException,
                ServletException {
       String username = ((User)auth.getPrincipal()).getEmail();
       String tokenSecret = webEnvironment.getProperty("token.secret");
       byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
       SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
       Instant now = Instant.now();
       String token = Jwts.builder()
               .subject(username)
               .expiration(Date.from(now.plusMillis(Long.parseLong(webEnvironment.getProperty("token.expiration_time")))))
               .issuedAt(Date.from(now))
               .signWith(secretKey)
               .compact();
      response.addHeader("token",token);
      response.addHeader("userId",username);


        }











    }




