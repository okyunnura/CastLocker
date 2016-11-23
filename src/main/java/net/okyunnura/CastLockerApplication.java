package net.okyunnura;

import net.okyunnura.config.AwsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AwsProperties.class)
@SpringBootApplication
public class CastLockerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CastLockerApplication.class, args);
	}
}
