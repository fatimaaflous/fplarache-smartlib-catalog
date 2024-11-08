package com.example.smartlib_catalog.configuration;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/comptes/add", "/api/comptes/{id}/update", "/api/comptes/{id}/delete").hasRole("ADMIN") // Fix path for update/delete
                .requestMatchers("/api/comptes/{id}","/api/comptes/crediter/{id}/{montant}", "/api/comptes/debiter/{id}/{montant}").authenticated() // Fixed the credit and debit paths
                .requestMatchers("/api/comptes/all").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic();  // Basic auth for testing

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password("{noop}admin").roles("ADMIN").build());
        manager.createUser(User.withUsername("user").password("{noop}user").roles("USER").build());
        return manager;
    }

}*/

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private  RsaConfig rsaConfig;
    private  PasswordEncoder passwordEncoder;

    public SecurityConfig(RsaConfig rsaConfig, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.rsaConfig = rsaConfig;
    }

    // service d'authentification
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }



    @Bean
    public UserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password(passwordEncoder.encode("admin123")).authorities("ADMIN").build(),
                User.withUsername("user").password(passwordEncoder.encode("user123")).authorities("USER").build()
        );
    }


    // Security filter chain for configuring HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API
                .authorizeRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Allow access to login
                        .requestMatchers("/refreshToken/**").permitAll() // Allow access to refresh token
                        .requestMatchers("/v1/livres/add", "/v1/livres/update", "/v1/livres/delete","/v1/livres/all").hasAuthority("SCOPE_ADMIN") // Admin routes
                        .requestMatchers("/v1/livres/{id}","/v1/livres").hasAuthority("SCOPE_USER") // All other account routes require authentication
                        .anyRequest().permitAll()) // Allow all other requests
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) // Enable JWT support
                .httpBasic(Customizer.withDefaults()) // Enable Basic Auth for testing
                .build();
    }



    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaConfig.publicKey()).privateKey(rsaConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaConfig.publicKey()).build();
    }





}

