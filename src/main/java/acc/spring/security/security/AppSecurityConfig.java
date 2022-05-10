package acc.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static acc.spring.security.security.UserPermission.*;
import static acc.spring.security.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AppSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	//BASIC AUTHENTICATION
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			csrf().disable()
			.authorizeRequests()
			.antMatchers("/","/index","/css/**","/js/**").permitAll()
			.antMatchers("/api/dog/**").hasRole(DOG.name())
			.anyRequest()
			.authenticated()
			.and()
			//Form Based Auth
			.formLogin()
			//Custom Login Page
			.loginPage("/login").permitAll()
			//Default URL once it is loggedIn
			.defaultSuccessUrl("/dogs",true)
		;
	}
	
	//How to retrieve users from the db
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		UserDetails mijoUser =  User.builder()
				.username("mijo")
				//.password("mijo")    // -> password without encoding
				.password(passwordEncoder.encode("mijo"))//Password now Encoded
				//.roles(DOG.name())
				.authorities(DOG.getGrantedAuthorities())
				.build();

		UserDetails manaUser =  User.builder()
				.username("mana")
				.password(passwordEncoder.encode("mana"))//Password now Encoded
				//.roles(SUPER_DOG.name())
				.authorities(SUPER_DOG.getGrantedAuthorities())
				.build();

		UserDetails kukiUser =  User.builder()
				.username("kuki")
				.password(passwordEncoder.encode("kuki"))//Password now Encoded
				//.roles(DOGGERINO.name())
				.authorities(DOGGERINO.getGrantedAuthorities())
				.build();
		
		return new InMemoryUserDetailsManager(mijoUser,manaUser,kukiUser);
	}
	
	
}
