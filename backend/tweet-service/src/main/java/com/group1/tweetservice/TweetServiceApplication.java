package com.group1.tweetservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.group1.tweetservice")
@EnableFeignClients
@EnableScheduling
public class TweetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetServiceApplication.class, args);
	}

}
