package restAssured.userAuth.jwt.userLogin.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public interface UsernamePasswordAuthentication {

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException;

    void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication auth, FilterChain chain) throws IOException,
                ServletException;
}
