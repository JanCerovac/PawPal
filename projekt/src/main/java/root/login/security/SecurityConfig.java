package root.login.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * privremeno spremanje podataka za login,
     * dok nemamo osposobljenu bazu podataka.
     * Spring Security zove ovu funkciju kako bi pronašao korisnike 'by username'
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        // 'demo' korisnik
        var user = User.withUsername("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build();

        // 'user store' (privremeno u memoriji)
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * PasswordEncoder koji se koristi
     * gdje god radimo sa šiframa
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * glavna Spring Security konfiguracija.
     * određuje što je javno, što zahtjeva login, itd.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ovi 'endpoints' ne zahtjevaju login
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // svi ostali zahtjevaju
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // naš 'endpoint' za login
                        .loginPage("/login")
                        // gdje idemo nakon uspješnog login-a
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        // url redirect za google login
                        .loginPage("/oauth2/authorization/google")
                        // gdje idemo nakon uspješnog login-a
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                )
                .logout(logout -> logout
                        // gdje idemo nakon logout-a
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
