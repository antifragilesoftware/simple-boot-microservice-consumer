package com.russmiles.antifragilesoftware.samples;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleBootMicroserviceApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "spring.cloud.bus.enabled=false", "spring.loud.discovery.enabled=false", "spring.cloud.consul.enabled=false"})
public class SimpleBootMicroserviceApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void canPerformSimpleGetRequestOnMicroservice() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Simple Boot Microservice Consumer Alive!"));
    }

    @Test
    public void canPerformDiscoveredServiceInstancesOnMicroservice() throws Exception {
        mockMvc.perform(get("/discoveredServiceInstances"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(""));
    }

    @Test(expected = NestedServletException.class)
    public void cannotInvokeDiscoveredServiceInstancesOnMicroserviceAsNoConsulPresent() throws Exception {
        mockMvc.perform(get("/invokeConsumedService"));
    }
}
