package org.example.loan.config;

import org.example.loan.web.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf-> csrf.ignoringRequestMatchers("/login"))
                .addFilterAt(
                        new JsonUsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests((authorize) -> authorize
                            .requestMatchers("/articles/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    //.httpBasic(Customizer.withDefaults()) basic認証は使わない
                    .formLogin(Customizer.withDefaults());

            return http.build();
        }

        /// user情報を取得するサービス
        /// user情報を持ってくるサービスをUserDetailsServiceという
        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

}
