package org.example.loan.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.loan.web.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
            , SecurityContextRepository securityContextRepository
            , SessionAuthenticationStrategy sessionAuthenticationStrategy
            , AuthenticationManager authenticationManager
            , ObjectMapper objectMapper
    ) throws Exception {
        http
                //.csrf(csrf -> csrf.ignoringRequestMatchers("/login"))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAt(
                        new JsonUsernamePasswordAuthenticationFilter(
                                securityContextRepository,
                                sessionAuthenticationStrategy,
                                authenticationManager,
                                objectMapper
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .securityContext(context -> context.securityContextRepository(securityContextRepository))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/articles/**").permitAll()
                        .anyRequest().authenticated()
                );
        //.formLogin(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    // SecurityContextRepositoryのBean登録
    // HttpSessionSecurityContextRepositoryをSecurityContextRepositoryという型でBean登録する
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // SessionAuthenticationStrategyのBean登録
    // ChangeSessionIdAuthenticationStrategyをSessionAuthenticationStrategyという型でBean登録する
    // Bean登録された型はsecurityFilterChainの引数に注入してくれます（=Bean取得）
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new ChangeSessionIdAuthenticationStrategy();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}
