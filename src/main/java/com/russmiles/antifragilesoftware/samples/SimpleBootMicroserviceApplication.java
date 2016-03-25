package com.russmiles.antifragilesoftware.samples;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableHystrix
public class SimpleBootMicroserviceApplication {

    @Value("${consumed.microservice.name}")
    private String consumedServiceName;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/")
    @HystrixCommand
    public String home() {
        return "Simple Boot Microservice Consumer Alive!";
    }

    @RequestMapping("/discoveredServiceInstances")
    public String serviceInstances() {
        StringBuffer discoveredServiceInstances = new StringBuffer();

        discoveryClient.getInstances(consumedServiceName).forEach((ServiceInstance s) -> {
            discoveredServiceInstances.append("Services Discovered: ").append(ToStringBuilder.reflectionToString(s)).append('\n');
        });

        return discoveredServiceInstances.toString();
    }

    @RequestMapping("/invokeConsumedService")
    public String invokeConsumedService() {
        ResponseEntity<String> exchange =
                this.restTemplate.exchange(
                        "http://microservice1/",
                        HttpMethod.GET,
                        null,
                        String.class);

        return exchange.getBody();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleBootMicroserviceApplication.class, args);
    }
}
