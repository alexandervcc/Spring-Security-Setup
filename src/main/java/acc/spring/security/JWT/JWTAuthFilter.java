package acc.spring.security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;


public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTConfig jwtConfig;

    public JWTAuthFilter(
        AuthenticationManager authenticationManager,
        JWTConfig jwtConfig
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }



    //Filter added into the filter chain, in order to provide a custom authentication
    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        try {
            UsernamePasswordAuthRequest authRequest = new ObjectMapper()
                .readValue(request.getInputStream(), UsernamePasswordAuthRequest.class);

            Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
                );

            /*
                authenticationManager -> check if user exists, and try to authenticate it if it does
                remember that the current provider is the fakeService which has some users at
                an arraylist
            */
            Authentication  authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //Token creation and adding into the reponses, if the filter sucess
    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException, ServletException {


        String token = Jwts.builder()
            .setSubject(authResult.getName())
            .claim("authorities",authResult.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
            .signWith(jwtConfig.getSecretKey())
            .compact();

        response.setHeader(
            jwtConfig.getAuthorizationHeader(),
            jwtConfig.getTokenPrefix()+token
        );
    }
}
