package acc.spring.security.JWT;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenVerifierFilter extends OncePerRequestFilter {
    private final JWTConfig jwtConfig;

    public TokenVerifierFilter(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        //Request rejection
        if(Strings.isNullOrEmpty(authHeader)||!authHeader.startsWith(jwtConfig.getTokenPrefix())){
            filterChain.doFilter(request,response);
            return;
        }

        try {
            String token = authHeader.replace(jwtConfig.getTokenPrefix(),"");

            Jws< Claims> claimsJws = Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();
            List<Map<String,String>> authorities = (List<Map<String,String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthoritySet =
                authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthoritySet
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (JwtException e){
            throw new IllegalStateException("Token cannot be trusted!");
        }

        //pass to the next filter in the chain
        filterChain.doFilter(request,response);
    }
}
