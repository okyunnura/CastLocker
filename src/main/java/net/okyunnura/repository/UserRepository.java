package net.okyunnura.repository;

import net.okyunnura.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
	User findOneByUsername(String username);

	Set<User> findAllByParent(User user);
}
