package vn.ecall.a3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AmazonS3Configuration {

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.AP_SOUTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.
                        create(AwsBasicCredentials.create(
                        "AKIAQ2LCZDR5UJS2PHX5",
                        "nZwWw1/c3kIopaOcYwI7hTkgS6xQcYtMw6r3kvau")))
                .build();
    }
}

//"AKIAQ2LCZDR5UJS2PHX5",
//"nZwWw1/c3kIopaOcYwI7hTkgS6xQcYtMw6r3kvau"