package net.okyunnura.web;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import net.okyunnura.config.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private final AwsProperties applicationProperties;

	@Autowired
	public UploadController(AwsProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String upload(Model model) {

//		Statement list = new Statement(Statement.Effect.Allow)
//				.withActions(S3Actions.AllS3Actions)
//				.withResources(new S3BucketResource(applicationProperties.getAwsS3BucketName()));

		String value = applicationProperties.getAwsS3BucketName();

		Policy policy = new Policy().withStatements();

		GetFederationTokenRequest request = new GetFederationTokenRequest();
		return "upload";
	}
}
