package com.wilderman.reviewer.utils;

import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

@Service
public class AmazonClient {

//	private S3Client s3client;

	private AmazonS3 s3Client;


	@Value("${aws.credentials.accessKey}")
	private String accessKey;
	@Value("${aws.credentials.secretKey}")
	private String secretKey;
	@Value("${aws.region}")
	private String region;

	@PostConstruct
	private void initializeAmazon() {
		System.setProperty("aws.accessKeyId", accessKey);
		System.setProperty("aws.secretKey", secretKey);

		this.s3Client = AmazonS3ClientBuilder.standard()
				.withRegion(region)
				.withCredentials(new SystemPropertiesCredentialsProvider())
				.build();
	}

	public byte[] getObject(String bucket, String key) throws IOException {
		S3Object s3Object = s3Client.getObject(bucket, key);
		return IOUtils.toByteArray(s3Object.getObjectContent());
	}
	
	public PutObjectResult putObject(PutObjectRequest req) {
		return s3Client.putObject(req);
	}
	
	public URL getUrl(String bucket, String key) {
		return s3Client.getUrl(bucket, key);
	}

}
