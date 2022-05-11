package acc.spring.security.JWT;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JWTConfig {
    private String secret;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public JWTConfig() {
    }

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }
}
