package net.okyunnura.web;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3BucketResource;
import com.amazonaws.auth.policy.resources.S3ObjectResource;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/download")
public class DownloadController {
	private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final AwsProperties applicationProperties;

	@Autowired
	public DownloadController(AwsProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	@RequestMapping(value = "/{token}", method = RequestMethod.GET)
	public String page(@PathVariable String token, Model model) {

		String bucketName = applicationProperties.getBucketName();

		Statement listActionStatement = new Statement(Statement.Effect.Allow)
				.withActions(S3Actions.ListObjects)
				.withResources(new S3BucketResource(bucketName),
						new S3ObjectResource(bucketName, token),
						new S3ObjectResource(bucketName, token + "/*"));

		Policy policy = new Policy().withStatements(listActionStatement);

		GetFederationTokenRequest request = new GetFederationTokenRequest()
				.withDurationSeconds(900)
				.withName(token)
				.withPolicy(policy.toJson());

		AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient();
		GetFederationTokenResult result = client.getFederationToken(request);

		Credentials credentials = result.getCredentials();
		logger.info("accesskey:" + credentials.getAccessKeyId());
		logger.info("secretkey:" + credentials.getSecretAccessKey());
		logger.info("sessiontoken:" + credentials.getSessionToken());
		logger.info("expiration:" + credentials.getExpiration().toString());

		System.out.println("aws_access_key_id = " + credentials.getAccessKeyId());
		System.out.println("aws_secret_access_key = " + credentials.getSecretAccessKey());
		System.out.println("aws_session_token = " + credentials.getSessionToken());

		model.addAttribute("token", token);
		model.addAttribute("backetName", bucketName);
		model.addAttribute("credentials", credentials);
		return "download";
	}
}
