package com.texastoc.security;

import com.google.common.collect.ImmutableList;
import com.texastoc.module.player.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurity {

  private final UserDetailsServiceImpl userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManagerBuilder authenticationManagerBuilder;

  public WebSecurity(UserDetailsServiceImpl userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      JwtTokenProvider jwtTokenProvider) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
    // @formatter:off
    http
        .cors().and()
        .csrf().disable()
        .headers().frameOptions().sameOrigin()
        .and()
        .authorizeRequests()
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v4/login")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v4/settings")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v4/password/reset")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")).permitAll()
        .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/*")).permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider))
        .addFilter(new JwtAuthorizationFilter(authenticationManager, jwtTokenProvider, userDetailsService));
    return http.build();
    // @formatter:on
  }

  @Autowired
  void registerProvider(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    corsConfiguration.addAllowedMethod(HttpMethod.PUT.name());
    corsConfiguration.addAllowedMethod(HttpMethod.DELETE.name());
    corsConfiguration.addAllowedMethod(HttpMethod.PATCH.name());
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOriginPatterns(ImmutableList.of("*"));
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
