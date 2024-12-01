package org.example.loan.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JsonUsernamePasswordAuthenticationFilter(
            SecurityContextRepository securityContextRepository
            , SessionAuthenticationStrategy sessionAuthenticationStrategy
            , AuthenticationManager authenticationManager
    ) {
        super();
        setSecurityContextRepository(securityContextRepository);
        setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        setAuthenticationSuccessHandler((req, resp, auth) -> {
            resp.setStatus(HttpServletResponse.SC_OK);
        });
        setAuthenticationManager(authenticationManager);
        setAuthenticationFailureHandler((req, resp, auth) -> {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //return UsernamePasswordAuthenticationToken.authenticated("dummy-user", "dummy-password", List.of());

//        if (this.postOnly && !request.getMethod().equals("POST")) {
//            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
//        }

        var objectMapper = new ObjectMapper();
        LoginRequest jsonRequest;

        try {
            jsonRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("failed to read request as json", e);
        }

        // a = condigion ? yes :no;
        var username = jsonRequest.username != null ? jsonRequest.username : "";
        var password = jsonRequest.password != null ? jsonRequest.password : "";

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private record LoginRequest(String username, String password) {

    }
}
