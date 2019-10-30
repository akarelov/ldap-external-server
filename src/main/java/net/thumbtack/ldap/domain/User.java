package net.thumbtack.ldap.domain;

import javax.persistence.*;

@Entity
@Table(name = "[user]")
public class User {
    @Id
    private String id;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }

    public User(String id, String password, Role role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
