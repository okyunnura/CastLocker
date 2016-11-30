package net.okyunnura.web;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.resources.S3BucketResource;
import com.amazonaws.auth.policy.resources.S3ObjectResource;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetFederationTokenRequest;
import com.amazonaws.services.securitytoken.model.GetFederationTokenResult;
import javafx.util.Pair;
import net.okyunnura.config.AwsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
		String prefix = token + "/";

		Statement listActionStatement = new Statement(Statement.Effect.Allow)
				.withActions(S3Actions.ListObjects)
				.withResources(new S3BucketResource(bucketName),
						new S3ObjectResource(bucketName, token),
						new S3ObjectResource(bucketName, prefix + "*"));

		Policy policy = new Policy().withStatements(listActionStatement);

		GetFederationTokenRequest request = new GetFederationTokenRequest()
				.withName(token)
				.withPolicy(policy.toJson());

		AWSSecurityTokenServiceClient client = new AWSSecurityTokenServiceClient();
		GetFederationTokenResult result = client.getFederationToken(request);

		Credentials credentials = result.getCredentials();
		logger.info("accesskey:" + credentials.getAccessKeyId());
		logger.info("secretkey:" + credentials.getSecretAccessKey());
		logger.info("sessiontoken:" + credentials.getSessionToken());
		logger.info("expiration:" + credentials.getExpiration().toString());

		LocalDateTime expire = LocalDateTime.now().plusHours(1);
		Date expiration = Date.from(expire.atZone(ZoneId.systemDefault()).toInstant());

		BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(credentials.getAccessKeyId(), credentials.getSecretAccessKey(), credentials.getSessionToken());
		AmazonS3 s3 = new AmazonS3Client(sessionCredentials);
		ListObjectsV2Result listResult = s3.listObjectsV2(bucketName, prefix);
		List<Pair> list = listResult.getObjectSummaries().stream().map(s3ObjectSummary -> new Pair<>(s3ObjectSummary.getKey().replace(prefix, ""), s3.generatePresignedUrl(bucketName, s3ObjectSummary.getKey(), expiration, HttpMethod.GET))).collect(Collectors.toList());

		model.addAttribute("list", list);

		return "download";
	}
}
