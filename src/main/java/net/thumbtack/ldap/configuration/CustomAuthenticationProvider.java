package net.thumbtack.ldap.configuration;

import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private LdapContextSource contextSource;

    @PostConstruct
    private void initContext() {
        contextSource = new LdapContextSource();
        contextSource.setUrl("ldaps://ipa00.thumbtack.lo:636");
        contextSource.afterPropertiesSet();
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = new StringBuilder()
                .append("uid=")
                .append(authentication.getName())
                .append(",cn=users,cn=accounts,dc=thumbtack,dc=lo")
                .toString();
        String password = authentication.getCredentials().toString();
        try {
            contextSource.getContext(name, password);
            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            System.out.println("Login failed\n" + e);
        }
        return null;
//        if (shouldAuthenticateAgainstThirdPartySystem()) {
//             use the credentials
//             and authenticate against the third-party system
//            return new UsernamePasswordAuthenticationToken(
//                    name, password, new ArrayList<>());
//        } else {
//            return null;
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}