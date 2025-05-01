package restAssured.userAuth.jwt.userLogin.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private Environment webEnvironment;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
        super(authenticationManager);
        this.webEnvironment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(webEnvironment.getProperty("authorization.token.header.name"));
        if ((authorizationHeader == null) || !authorizationHeader.startsWith(webEnvironment.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken;
        authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(webEnvironment.getProperty("authorization.token.header.name"));
        if (authorizationHeader == null) {
            return null;
        }
        String token = authorizationHeader.replace(webEnvironment.getProperty("authorization.token.header.prefix"), "").trim();
        String tokenSecret = webEnvironment.getProperty("token.secret");
        if (tokenSecret == null) {
            return null;
        }
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        Jwt<?, ?> jwtObject = jwtParser.parse(token);
        String userId = ((Claims) jwtObject.getPayload()).getSubject();
        if (userId == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
    }

}
