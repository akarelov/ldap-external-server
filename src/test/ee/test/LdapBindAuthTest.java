package ee.test;

import org.junit.Test;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.ldap.LdapUtils;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class LdapBindAuthTest {
    @Test
    public void ldapTemplate() throws Exception {
        LdapTemplate ldapTemplate = new LdapTemplate();
        LdapContextSource ctx = new LdapContextSource();
        ctx.setAnonymousReadOnly(true);
        ctx.setUrl("ldaps://ipa00.thumbtack.lo:636/dc=thumbtack,dc=lo");
        ctx.afterPropertiesSet();

        ldapTemplate.setContextSource(ctx);
        LdapName name = new LdapName("uid=artem_karelov");

        boolean authenticate = ldapTemplate.authenticate(name, "(uid={0}, cn=users, cn=accounts, dc=thumbtack, dc=lo)", "Gar73jdg86#$aD12");
        System.out.println(authenticate);

        List<String> search = ldapTemplate.search(
                query().where("memberOf").is("cn=thumbtack-saratov,cn=groups,cn=accounts,dc=thumbtack,dc=lo"),
                (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
        System.out.println(search);
        search.sort(String::compareTo);
    }

    @Test
    public void ldapAuthBind() throws NamingException {
        DirContext ctx = null;
//        contextSource.setAnonymousReadOnly(false);
//        contextSource.setBase("uid=artem_karelov,cn=users,cn=accounts,dc=thumbtack,dc=lo");
//        contextSource.setPassword("Gar73jdg86#$aD12");
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldaps://ipa00.thumbtack.lo:636");
        contextSource.afterPropertiesSet();
        try {
            ctx = contextSource.getContext("uid=artem_karelov,cn=users,cn=accounts,dc=thumbtack,dc=lo", "Gar73jdg86#$aD12");
            System.out.println(ctx);
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            System.out.println("Login failed\n" + e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        LdapTemplate ldapTemplate = new LdapTemplate();
        ldapTemplate.setContextSource(contextSource);

        Name name = LdapNameBuilder.newInstance("dc=thumbtack,dc=lo")
                .add("cn", "accouts")
                .add("cn", "users")
                .add("uid", "artem_karelov")
                .build();
    }
}
