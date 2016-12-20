package net.okyunnura.web;

import net.okyunnura.config.AwsProperties;
import net.okyunnura.entity.User;
import net.okyunnura.repository.UserRepository;
import net.okyunnura.service.AuthorizedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/startup")
public class StartupController {
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final AwsProperties applicationProperties;

	private final AuthorizedService authorizedService;

	private final UserRepository userRepository;

	@Autowired
	public StartupController(AwsProperties applicationProperties, AuthorizedService authorizedService, UserRepository userRepository) {
		this.applicationProperties = applicationProperties;
		this.authorizedService = authorizedService;
		this.userRepository = userRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "startup";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String generate(@RequestParam String password, RedirectAttributes redirectAttributes) {
		String token = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
		LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		User user = new User();
		user.setUsername(token);
		user.setPassword(encoder.encode(password));
		user.setRole(User.Role.UPLOAD);
		user.setExpiredAt(expiredAt);

		userRepository.saveAndFlush(user);

		UserDetails userDetails = authorizedService.loadUserByUsername(token);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addAttribute("token", token);
		return "redirect:/upload/{token}";
	}
}
