package net.okyunnura.service;

import net.okyunnura.entity.AuthorizedUser;
import net.okyunnura.entity.User;
import net.okyunnura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizedService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public AuthorizedService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(username);
		return new AuthorizedUser(user);
	}
}
