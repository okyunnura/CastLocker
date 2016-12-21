package net.okyunnura.web;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3ObjectResource;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import net.okyunnura.config.AwsProperties;
import net.okyunnura.entity.User;
import net.okyunnura.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class UploadController {
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final AwsProperties applicationProperties;

	private final UserRepository userRepository;

	@Autowired
	public UploadController(AwsProperties applicationProperties, UserRepository userRepository) {
		this.applicationProperties = applicationProperties;
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/{token}", method = RequestMethod.GET)
	public String describe(@PathVariable String token, Model model) {

		String bucketName = applicationProperties.getBucketName();
		String prefix = token + "/";

		Statement allActionStatement = new Statement(Statement.Effect.Allow)
				.withActions(S3Actions.AllS3Actions)
				.withResources(new S3ObjectResource(bucketName, prefix + "*"));

		Policy policy = new Policy().withStatements(allActionStatement);

		GetFederationTokenRequest request = new GetFederationTokenRequest()
				.withDurationSeconds(3600)
				.withName(token)
				.withPolicy(policy.toJson());

		AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient();
		GetFederationTokenResult result = client.getFederationToken(request);

		Credentials credentials = result.getCredentials();

		model.addAttribute("token", token);
		model.addAttribute("bucketName", bucketName);
		model.addAttribute("credentials", credentials);
		return "upload";
	}

	@RequestMapping(value = "/downloader/{token}", method = RequestMethod.POST)
	public String downloader(@PathVariable String token,
							 @RequestParam String password,
							 @RequestParam LocalDateTime expiredAt,
							 Model model,
							 RedirectAttributes redirectAttributes) {

		User authentication = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();

		StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		User user = new User();
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setRole(User.Role.DOWNLOAD);
		user.setExpiredAt(expiredAt);
		user.setParent(authentication);

		userRepository.saveAndFlush(user);

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addAttribute("token", token);
		return "redirect:/upload/{token}";
	}
}
