package acc.spring.security.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static acc.spring.security.security.UserPermission.*;

public enum UserRole {
    DOG(Sets.newHashSet()),
    SUPER_DOG(Sets.newHashSet(DOG_READ,DOG_WRITE,FOOD_READ,FOOD_WRITE)),
    DOGGERINO(Sets.newHashSet(DOG_READ,FOOD_READ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> collection = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        collection.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return collection;
    }
}
