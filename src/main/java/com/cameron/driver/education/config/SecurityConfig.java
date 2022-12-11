package com.cameron.driver.education.config;


import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cameron.driver.education.constant.ROLES;

@Configuration
@EnableWebSecurity()
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private DataSource dataSource;
	
	@Autowired 
	private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public DataSource ds() {
//        return DataSourceBuilder.create().build();
//    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and().

				authorizeRequests().
				antMatchers("/employee/all/**").hasAnyRole(ROLES.ADMIN.toString())
				.antMatchers("/employee/findByName/**").hasAnyRole(ROLES.ADMIN.toString())
				.antMatchers("/employee/countapprovals/**").hasAnyRole(ROLES.ADMIN.toString())
				.antMatchers("/exam/allTestQuestion/**").permitAll()
				.antMatchers("/exam/testQuestionCount/**").permitAll()
				.antMatchers("/student/email/**").permitAll()
				.antMatchers("/exam/all/**").permitAll()
				.antMatchers("/exam/testresultcreate").permitAll()
				.antMatchers("/exam/testresultdetailcreate").permitAll()
				.antMatchers("/exam/testresultbymailtest/**").permitAll()
				.antMatchers("/exam/testresult/**").permitAll()
				.antMatchers("/exam/testresultdetail/**").permitAll()
				.anyRequest().authenticated()
				.and().httpBasic().authenticationEntryPoint(authenticationEntryPoint) .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	 @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	    {
	        auth.jdbcAuthentication().dataSource(dataSource)
	            .usersByUsernameQuery("select USERNAME, PASSWORD, 1 as enabled  from EMPLOYEE_USER where USERNAME=? and STATUS='ACTIVE'")
	        .authoritiesByUsernameQuery("select USERNAME,ROLE from EMPLOYEE_USER where USERNAME=? and STATUS='ACTIVE'");
	           
	    }
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	/*
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder auth)
	 * throws Exception {
	 * auth.inMemoryAuthentication().withUser("admin").password("{noop}password").
	 * roles("USER").and().withUser("Jagjit").password("{noop}Singh").roles("ADMIN")
	 * ; }
	 */	    
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        final CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowCredentials(true);
	        configuration.setAllowedMethods(Arrays.asList("HEAD",
	            "GET", "POST", "PUT", "DELETE", "PATCH"));
	        // setAllowCredentials(true) is important, otherwise:
	        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
	        configuration.setAllowCredentials(true);
	        // setAllowedHeaders is important! Without it, OPTIONS preflight request
	        // will fail with 403 Invalid CORS request
	        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
	        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	}
	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable().
//                authorizeRequests()
//                .antMatchers("/**")
//                .permitAll()
//                .anyRequest().authenticated().and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
// 
//        http.headers().frameOptions().disable();
//    }
    
 
   
}