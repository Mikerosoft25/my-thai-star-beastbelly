package com.devonfw.application.mtsj.general.service.impl.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.devonfw.application.mtsj.general.common.impl.security.BaseUserDetailsService;
import com.devonfw.application.mtsj.general.common.impl.security.twofactor.TwoFactorAuthenticationProvider;
import com.devonfw.application.mtsj.usermanagement.logic.base.AdvancedDaoAuthenticationProvider;
import com.devonfw.application.mtsj.usermanagement.logic.base.JWTAuthenticationFilter;
import com.devonfw.application.mtsj.usermanagement.logic.base.JWTLoginFilter;
import com.devonfw.application.mtsj.usermanagement.logic.base.TwoFactorFilter;

/**
 * This type serves as a base class for extensions of the {@code WebSecurityConfigurerAdapter} and provides a default
 * configuration. <br/>
 * Security configuration is based on {@link WebSecurityConfigurerAdapter}. This configuration is by purpose designed
 * most simple for authentication based on Json Web Token.
 */
public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${security.cors.enabled}")
  boolean corsEnabled = true;

  @Inject
  private BaseUserDetailsService userDetailsService;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Bean
  public AdvancedDaoAuthenticationProvider advancedDaoAuthenticationProvider() {

    AdvancedDaoAuthenticationProvider authProvider = new AdvancedDaoAuthenticationProvider();
    authProvider.setPasswordEncoder(this.passwordEncoder);
    authProvider.setUserDetailsService(this.userDetailsService);
    return authProvider;
  }

  @Bean
  public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider() {

    TwoFactorAuthenticationProvider authProvider = new TwoFactorAuthenticationProvider();
    authProvider.setPasswordEncoder(this.passwordEncoder);
    return authProvider;
  }

  @Inject
  private AdvancedDaoAuthenticationProvider advancedDaoAuthenticationProvider;

  @Inject
  private TwoFactorAuthenticationProvider twoFactorAuthenticationProvider;

  private CorsFilter getCorsFilter() {

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("OPTIONS");
    config.addAllowedMethod("HEAD");
    config.addAllowedMethod("GET");
    config.addAllowedMethod("PUT");
    config.addAllowedMethod("POST");
    config.addAllowedMethod("DELETE");
    config.addAllowedMethod("PATCH");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  /**
   * Configure spring security to enable login with JWT.
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {

    // this line only for testing h2-console in web
    http.headers().frameOptions().disable();

    // /h2-console/** only for testing h2-console in web
    String[] unsecuredResources = new String[] { "/login", "/security/**", "/services/rest/login",
    "/services/rest/logout", "/services/rest/dishmanagement/**", "/services/rest/imagemanagement/**",
    "/services/rest/ordermanagement/v1/order", "/services/rest/bookingmanagement/v1/booking",
    "/services/rest/bookingmanagement/v1/booking/cancel/**",
    "/services/rest/bookingmanagement/v1/invitedguest/accept/**",
    "/services/rest/bookingmanagement/v1/invitedguest/decline/**",
    "/services/rest/ordermanagement/v1/order/cancelorder/**", "/services/rest/ordermanagement/v1/order/**",
    "/h2-console/**", "/services/rest/usermanagement/v1/user/register",
    "/services/rest/bookingmanagement/v1/booking/**", "/services/rest/ordermanagement/v1/orderstatus/**",
    "/services/rest/ordermanagement/v1/orderpaystatus/**", "/services/rest/bookingmanagement/v1/table/search",
    "/services/rest/usermanagement/v1/user/changerole", "/alexa/**", "/alexa",
    "/services/rest/bookingmanagement/v1/table/search", "/services/rest/usermanagement/v1/user/changerole",
    "/services/rest/usermanagement/v1/user/changeuser", "/services/rest/usermanagement/v1/user/resetPassword",
    "/services/rest/usermanagement/v1/user/changePassword", "/services/rest/usermanagement/v1/userrole/**",
    "/services/rest/ordermanagement/v1/alexadeliveryorderfromhome",
    "/services/rest/ordermanagement/v1/alexaorderfromhome", "/services/rest/ordermanagement/v1/alexaorderinhouse",
    "/services/rest/usermanagement/v1/user/resettoken/*", "/services/rest/bookingmanagement/v1/invitedguest/accept/*",
    "/services/rest/bookingmanagement/v1/invitedguest/decline/*",
    "/services/rest/bookingmanagement/v1/bookingbyguesttoken/*" };

    http.userDetailsService(this.userDetailsService).csrf().disable().exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
        .antMatchers(unsecuredResources).permitAll().antMatchers(HttpMethod.POST, "/login").permitAll().anyRequest()
        .authenticated().and()
        // verification with OTP are filtered with the TwoFactorFilter
        .addFilterBefore(new TwoFactorFilter("/verify", authenticationManager(), this.userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        // the api/login requests are filtered with the JWTLoginFilter
        .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), this.userDetailsService),
            UsernamePasswordAuthenticationFilter.class)
        // other requests are filtered to check the presence of JWT in header
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    if (this.corsEnabled) {
      http.addFilterBefore(getCorsFilter(), CsrfFilter.class);
    }
  }

  @Override
  @SuppressWarnings("javadoc")
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.authenticationProvider(this.advancedDaoAuthenticationProvider)
        .authenticationProvider(this.twoFactorAuthenticationProvider);
  }
}
