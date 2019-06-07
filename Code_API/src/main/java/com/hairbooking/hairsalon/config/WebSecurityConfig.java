//package com.hairbooking.hairsalon.config;
//
//import com.hairbooking.hairsalon.filter.JWTAuthenticationFilter;
//import com.hairbooking.hairsalon.filter.JWTLoginFilter;
//import com.hairbooking.hairsalon.impl.AccountServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    AccountServiceImpl accountService;
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests()
//                // No need authentication.
//                .antMatchers("/","/login").permitAll() //
//                .antMatchers(HttpMethod.POST, "/login").permitAll() //
//                .antMatchers(HttpMethod.GET, "/login").permitAll() // For Test on Browser
//                // Need authentication.
//                .anyRequest().authenticated()
//                //
//                .and()
//                //
//                // Add Filter 1 - JWTLoginFilter
//                //
//                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
//                        UsernamePasswordAuthenticationFilter.class)
//                //
//                // Add Filter 2 - JWTAuthenticationFilter
//                //
//                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        return bCryptPasswordEncoder;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//
//        // Setting Service to find User in the database.
//        // And Setting PassswordEncoder
//        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
//        String encrytedPassword = this.passwordEncoder().encode(password);
////        System.out.println("Encoded password of 123=" + encrytedPassword);
//
//        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> //
//                mngConfig = auth.inMemoryAuthentication();
//
//
//        UserDetails u1 = User.withUsername("tom").password(encrytedPassword).roles("USER").build();
//        UserDetails u2 = User.withUsername("jerry").password(encrytedPassword).roles("USER").build();
//
//        mngConfig.withUser(u1);
//        mngConfig.withUser(u2);
//
//
//
//    }
//}
