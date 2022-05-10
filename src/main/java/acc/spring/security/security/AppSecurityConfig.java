package acc.spring.security.security;

import acc.spring.security.auth.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static acc.spring.security.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

	private final PasswordEncoder passwordEncoder;
	private final AppUserService appUserService;

	public AppSecurityConfig(PasswordEncoder passwordEncoder, AppUserService appUserService) {
		this.passwordEncoder = passwordEncoder;
		this.appUserService = appUserService;
	}

	@Autowired


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
				.loginPage("/login")
				.permitAll()
				.defaultSuccessUrl("/dogs",true)
				//Parameters keys for the http request
				.passwordParameter("password")
				.usernameParameter("username")
			.and()
			//Default of two weeks, but can be customized
			.rememberMe()
				.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(7))
				.key("CheemsSecret")	//Key for the md5 hash
				.rememberMeParameter("remember-me")
			.and()
			//Custom Logout
			.logout()
				.logoutUrl("/logout")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
				.clearAuthentication(true)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID","remember-me")
				.logoutSuccessUrl("/login")
		;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(appUserService);
		return provider;
	}
	
	
}
