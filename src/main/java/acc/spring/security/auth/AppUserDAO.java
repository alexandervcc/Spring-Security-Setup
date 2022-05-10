package acc.spring.security.auth;


import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface AppUserDAO {
    public Optional<User> selectUserByUsername(String username);
}
