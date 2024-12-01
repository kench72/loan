package org.example.loan.config;

import org.example.loan.web.filter.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                                authenticationManager
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
    public AuthenticationManager authenticationManager(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {

        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        // ProviderManagerはAuthenticationManagerの実装クラス
        // Providerを詰めてAuthenticationManagerに入れる。
        return new ProviderManager(provider);
    }

    /// user情報を取得するサービス
    /// user情報を持ってくるサービスをUserDetailsServiceという
    @Bean
    public UserDetailsService userDetailsService() {
        // UserDetails userDetails = User.withDefaultPasswordEncoder()
        UserDetails userDetails = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        // ダミー実装：起動するたびに上記ダミーのユーザーをinMemoryに追加
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }
}
