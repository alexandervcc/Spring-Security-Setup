package acc.spring.security.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import static acc.spring.security.security.UserRole.*;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeAppUserDAOService  implements AppUserDAO{

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeAppUserDAOService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> selectUserByUsername(String username) {
        return getUsers()
            .stream()
            .filter(user -> username.equals(user.getUsername()))
            .findFirst();
    }

    private List<User> getUsers(){
        List<User> listUsers = Lists.newArrayList(
            new User(
                SUPER_DOG.getGrantedAuthorities(),
                passwordEncoder.encode("mana"),
                "mana",
                true,
                true,
                true,
                true
            ),
            new User(
                DOGGERINO.getGrantedAuthorities(),
                passwordEncoder.encode("kuki"),
                "kuki",
                true,
                true,
                true,
                true
            ),
            new User(
                DOG.getGrantedAuthorities(),
                passwordEncoder.encode("mijo"),
                "mijo",
                true,
                true,
                true,
                true
            )
        );
        return listUsers;
    }
}
