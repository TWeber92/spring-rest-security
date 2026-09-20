package com.infy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//		.withUser("smith")
//		.password(passwordEncoder().encode("smith123"))
//		.roles("ADMIN")
//		.and().withUser("tim")
//		.password(passwordEncoder().encode("tim123"))
//		.roles("USER");
//	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//		.antMatchers(HttpMethod.GET, "/infybank/customers/**")
//		.hasRole("USER")
//		.anyRequest().authenticated()
//		.and().httpBasic();
//		http.csrf().disable();
//	}
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}
@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/infybank/customers")
				.hasAnyRole("ADMIN", "USER")
				.requestMatchers("/infybank/customer/**")
				.hasRole("ADMIN")
				.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	protected InMemoryUserDetailsManager userDetailsService() throws Exception {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User
				.withUsername("smith")
				.password(passwordEncoder().encode("smith123"))
				.roles("ADMIN")
				.build());
		manager.createUser(User
				.withUsername("tim")
				.password(passwordEncoder().encode("tim123"))
				.roles("USER")
				.build());
		return manager;
	}
}
