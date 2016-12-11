package net.okyunnura.web;

import net.okyunnura.config.AwsProperties;
import net.okyunnura.entity.User;
import net.okyunnura.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/startup")
public class StartupController {
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final AwsProperties applicationProperties;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public StartupController(AwsProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "startup";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String generate(@RequestParam String password, RedirectAttributes redirectAttributes) {
		String token = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		User user = new User();
		user.setUsername(token);
		user.setPassword(password);
		user.setRole(User.Role.UPLOAD);

		userRepository.save(user);

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addAttribute("token", token);
		return "redirect:/upload/{token}";
	}
}
