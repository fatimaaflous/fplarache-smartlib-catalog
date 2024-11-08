package com.example.smartlib_catalog.controller;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Oauth2Controller {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;

    public Oauth2Controller(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        // Authenticate the user

        String username = body.get("username");
        String password = body.get("password");
        System.out.println("Received username: " + username + ", password: " + password);
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Generate tokens
        Instant instant = Instant.now();

        // Get user scopes
        String scopes = authenticate.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(" "));

        // Create access token
        JwtClaimsSet jwtClaimsSetAccessToken = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                .claim("name", authenticate.getName())
                .claim("scope", scopes)
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetAccessToken)).getTokenValue();

        // Create refresh token
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(authenticate.getName())
                .issuedAt(instant)
                .expiresAt(instant.plus(15, ChronoUnit.MINUTES))
                .claim("name", authenticate.getName())
                .build();

        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefreshToken)).getTokenValue();

        // Prepare response tokens
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", accessToken);
        tokenResponse.put("refresh_token", refreshToken);

        return tokenResponse;
    }

    @PostMapping("/refreshToken")
    public Map<String, String> refreshToken(@RequestParam String refreshToken) {
        if (refreshToken == null) {
            return Map.of("message", "Refresh token is required");
        }

        // Validate the refresh token
        Jwt decoded = jwtDecoder.decode(refreshToken);
        String username = decoded.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate new access token
        Instant instant = Instant.now();
        String scopes = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSetAccessToken = JwtClaimsSet.builder()
                .issuer("MS_sec")
                .subject(userDetails.getUsername())
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                .claim("name", userDetails.getUsername())
                .claim("scope", scopes)
                .build();

        String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetAccessToken)).getTokenValue();

        // Prepare response tokens
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", newAccessToken);
        tokenResponse.put("refresh_token", refreshToken);

        return tokenResponse;
    }
}
