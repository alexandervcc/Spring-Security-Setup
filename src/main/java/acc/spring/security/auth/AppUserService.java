package acc.spring.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Layer to access db for authentication

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserDAO appUserDAO;

    @Autowired
    public AppUserService(@Qualifier("fake") AppUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserDAO.selectUserByUsername(username).orElseThrow(
            ()->new UsernameNotFoundException(String.format("Username %s NOT Found",username))
        );
    }
}
