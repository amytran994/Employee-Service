package com.example.EmployeeService.config;

import com.example.EmployeeService.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtFilter jwtFilter;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

//    @Override
//    @Bean
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/employee/all", "employee/firstname", "/employee/lastname").hasAuthority("HR")

                .antMatchers("/employee/new", "/employee/{employeeId}", "/employee/{employeeId}/documents/upload",
                        "/employee/{employeeId}/documents/download/{id}", "/employee/{employeeId}/documents",
                        "/employee/{employeeId}", "/employee/employeeNameId/{userId}").permitAll()
                .anyRequest()
                .authenticated();
    }
}
