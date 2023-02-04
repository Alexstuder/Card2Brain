package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.dto.HealthCheckInfoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for checking if the application is starting correctly
 */


@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.buildtime}")
    private String buildTime;

    /**
     * Test method to checks if tha application is startet and the controlller shows the correct html-code
     *
     * @throws Exception - in case of a failure in performing the request.
     */

    @Test
    void getHealthCheck() throws Exception {

        //arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/healthCheck").accept(MediaType.TEXT_HTML);

        //act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //assert
        assertTrue(result.getResponse().getContentAsString().contains("""
                <body><h1>Card2Brain is running!</h1>"""));

    }

    @Test
    void getHealthCheckInfos() throws Exception {

        //arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/healthCheck/infos").accept(MediaType.TEXT_HTML);

        //act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //assert
        TypeReference<HealthCheckInfoDto> mapType = new TypeReference<>() {
        };
        HealthCheckInfoDto healthCheckInfoDto = objectMapper.readValue(result.getResponse().getContentAsString(), mapType);

        assertEquals("Card2Brain is running", healthCheckInfoDto.getAPP_IS_RUNNING());
        assertEquals(appVersion, healthCheckInfoDto.getAppVersion());
        assertEquals(buildTime, healthCheckInfoDto.getBuildTime());
        assertEquals("http://localhost:9001/swagger-ui.html", healthCheckInfoDto.getSwaggerUrl());
    }
}
