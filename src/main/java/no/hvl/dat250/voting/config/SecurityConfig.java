package no.hvl.dat250.voting.config;

import lombok.RequiredArgsConstructor;
import no.hvl.dat250.voting.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;



    /*@Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/api/user/register").permitAll()
            .requestMatchers("/api/user/login").permitAll()
            .requestMatchers("/api/polls/public").permitAll()

            .requestMatchers("/api/user/**").hasRole("USER")
            .requestMatchers("/api/groups/**").hasRole("USER")

            .anyRequest().authenticated()
                .and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login").permitAll()
                        .requestMatchers("/api/user/register").permitAll()
                        .requestMatchers("/api/polls/public").permitAll()
                        .requestMatchers("/api/polls/vote").permitAll()
                        .requestMatchers("/api/polls/**").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("/api/votes/**").permitAll()
                        .requestMatchers("/api/groups/**").permitAll()


                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /*@Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll())
                .csrf().disable();

        return  http.build();
    }*/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception  {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
