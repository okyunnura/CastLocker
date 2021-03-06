package net.okyunnura.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(AwsProperties.PREFIX)
public class AwsProperties {
	public static final String PREFIX = "aws";
	private String accessKeyId;
	private String secretKey;
	private String bucketName;
}
