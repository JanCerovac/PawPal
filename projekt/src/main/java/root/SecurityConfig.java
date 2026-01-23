package root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import root.database.repositories.UserRepository;
import root.services.login.GoogleOidcUserService;
import root.services.login.LoginFailureHandler;
import root.services.login.PawPalUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginFailureHandler loginFailureHandler;
    private final GoogleOidcUserService googleOidcUserService;
    private final PawPalUserDetailsService userDetailsService;

    public SecurityConfig(
            LoginFailureHandler loginFailureHandler,
            GoogleOidcUserService googleOidcUserService,
            PawPalUserDetailsService userDetailsService
    ) {
        this.loginFailureHandler = loginFailureHandler;
        this.googleOidcUserService = googleOidcUserService;
        this.userDetailsService = userDetailsService;
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
                // privremeno dopušta 'requests' bez csrf
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/admin/price", "/admin/delete", "/reservation/accept", "/walk/walker/delete", "/edit/owner/delete", "/search", "/setup", "/ws/**", "/", "/notifications/response", "/delete")  // or "/api/**"
                )
                // naš 'interceptor'
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // ovi 'endpoints' ne zahtjevaju login
                        .requestMatchers(
                                "/ws/**", "/users", "/register", "/login", "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        // svi ostali zahtjevaju
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // naš 'endpoint' za login
                        .loginPage("/login")
                        // 'interceptor' za neuspješan login
                        .failureUrl("/login?error")
                        .failureHandler(loginFailureHandler)
                        // gdje idemo nakon uspješnog login-a
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        // url redirect za google login
                        .loginPage("/oauth2/authorization/google")
                        // naš 'interceptor'
                        .userInfoEndpoint(ui -> ui.oidcUserService(googleOidcUserService))
                        .failureHandler((request, response, ex) -> {
                            ex.printStackTrace();
                            request.getSession(true).setAttribute("OAUTH_ERROR", ex.getMessage());
                            response.sendRedirect("/login?error");
                        })
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
