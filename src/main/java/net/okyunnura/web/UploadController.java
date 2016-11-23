package net.okyunnura.web;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3BucketResource;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import net.okyunnura.config.AwsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class UploadController {
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final AwsProperties applicationProperties;

	@Autowired
	public UploadController(AwsProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String upload(Model model) {

		String token = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

		Statement listStatement = new Statement(Statement.Effect.Allow)
				.withActions(S3Actions.AllS3Actions)
				.withResources(new S3BucketResource(applicationProperties.getS3BucketName()));

		Policy policy = new Policy().withStatements(listStatement);

		GetFederationTokenRequest request = new GetFederationTokenRequest()
				.withDurationSeconds(3600)
				.withName(token)
				.withPolicy(policy.toJson());

		AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient(new BasicAWSCredentials(applicationProperties.getAccessKey(), applicationProperties.getSecretKey()));
		GetFederationTokenResult result = client.getFederationToken(request);

		Credentials credentials = result.getCredentials();
		logger.info("accesskey:"+credentials.getAccessKeyId());
		logger.info("secretkey:"+credentials.getSecretAccessKey());
		logger.info("sessiontoken:"+credentials.getSessionToken());
		logger.info("expiration:"+credentials.getExpiration().toString());

		return "upload";
	}
}
