package net.thumbtack.ldap.repository;

import net.thumbtack.ldap.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findById(String uid);
}
