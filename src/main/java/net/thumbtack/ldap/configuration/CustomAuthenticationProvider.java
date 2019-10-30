package net.thumbtack.ldap.configuration;

import net.thumbtack.ldap.domain.Role;
import net.thumbtack.ldap.domain.User;
import net.thumbtack.ldap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private LdapContextSource contextSource;

    @Autowired
    private UserRepository userRepository;


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
            userRepository.save(new User(name, password, Role.EMPLOYEE));
            Role roles = userRepository.findById(name).getRole();
            return new UsernamePasswordAuthenticationToken(name, password, Arrays.asList(roles));
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            System.out.println("Login failed\n" + e);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}