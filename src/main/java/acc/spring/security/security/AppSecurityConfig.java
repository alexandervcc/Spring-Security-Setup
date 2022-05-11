package acc.spring.security.security;

import acc.spring.security.JWT.JWTAuthFilter;
import acc.spring.security.JWT.JWTConfig;
import acc.spring.security.JWT.TokenVerifierFilter;
import acc.spring.security.auth.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static acc.spring.security.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{

	private final PasswordEncoder passwordEncoder;
	private final AppUserService appUserService;
	private final JWTConfig jwtConfig;

	@Autowired
	public AppSecurityConfig(
		PasswordEncoder passwordEncoder,
		AppUserService appUserService,
		JWTConfig jwtConfig
	) {
		this.passwordEncoder = passwordEncoder;
		this.appUserService = appUserService;
		this.jwtConfig = jwtConfig;
	}


	//BASIC AUTHENTICATION
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			csrf().disable()
			//Stateless for the JWT
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			//Add JWT Auth into the filter chain
			.addFilter(new JWTAuthFilter(authenticationManager(),jwtConfig))
			//Add the JWT verifier after the JWT AuthFilter
			.addFilterAfter(new TokenVerifierFilter(jwtConfig),JWTAuthFilter.class)
			.authorizeRequests()
			.antMatchers("/","/index","/css/**","/js/**").permitAll()
			.antMatchers("/api/dog/**").hasRole(DOG.name())
			.anyRequest()
			.authenticated()
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
