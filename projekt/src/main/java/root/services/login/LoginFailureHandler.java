package root.services.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Prilagođeni 'handler' za neuspješan login
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex
    ) throws IOException, ServletException {
        String msg = ex.getMessage();

        // dodajemo error msg u session atribut
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION_MESSAGE", msg);

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, ex);
    }
}
