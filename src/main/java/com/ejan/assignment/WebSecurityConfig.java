package com.ejan.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ejan.assignment.service.impl.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
    
    public DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	authProvider.setUserDetailsService(userDetailsService());
    	authProvider.setPasswordEncoder(passwordEncoder());
    	return authProvider;
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	logger.info("configure global");
    	auth.authenticationProvider(authenticationProvider());
    	
    }
    
    @Bean
	public PasswordEncoder passwordEncoder() {
		logger.info("passwordEncoder");
		return new BCryptPasswordEncoder();
	}
    

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.formLogin()
			.loginPage("/login")
			.failureForwardUrl("/login")
			.and().logout()
			.logoutSuccessUrl("/login")
			.and()
			.authorizeRequests()
			.antMatchers("/", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
//			.antMatchers("/admin/**").hasAnyRole("ADMIN")
//			.antMatchers("/user/**").hasAnyRole("USER")
			.and()
			.csrf().disable()
			.sessionManagement()
			.maximumSessions(1)
			.expiredUrl("/login?expired-session=true")
			.and()
			.invalidSessionUrl("/login?invalid-session=true");
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
 
}
