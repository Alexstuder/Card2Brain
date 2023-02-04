package ch.zhaw.card2brain.controller;

import ch.zhaw.card2brain.dto.HealthCheckInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * HealthCheck controller is a controller which shows that the application is running succsesfull
 * This class provides a method to check by returning a html response containing
 * the version, build time, and the server port.
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
@RestController
public class HealthCheckController {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.buildtime}")
    private String buildTime;

    @Value("${server.port}")
    private String serverPort;

    @Value("${card2brain.prod.server.address}")
    private String prodIp;


    /**
     * HealthCheck endpoint to return the health check information of the application.
     *
     * @return a string containing the health check information in html format
     * @throws throws IOExceptions
     */

    @GetMapping("/healthCheck")
    public String healthCheck() throws IOException {
        String htmlHealthCheck = """
                <!DOCTYPE html>
                <html>
                <head><title>Card2Brain - Healthcheck</title></head>
                <title></title>
                <body><h1>Card2Brain is running!</h1>
                <a href=""" + getUrl() + """
                >Swagger</a>
                <p>App.Version : :APP_VERSION</p> <p> BuildTime :BUILD_TIME</p></body></html>""";
        return htmlHealthCheck.replace("PORT", serverPort).replace("APP_VERSION", appVersion).replace("BUILD_TIME", buildTime);
    }

    @GetMapping("/healthCheck/infos")
    public String healthCheckinfo() throws IOException {

        return getHealthCheckInfo();
    }

    private String getHealthCheckInfo() throws JsonProcessingException {
        HealthCheckInfoDto healthCheckInfoDto = new HealthCheckInfoDto();

        healthCheckInfoDto.setAppVersion(appVersion);
        healthCheckInfoDto.setSwaggerUrl(getUrl());
        healthCheckInfoDto.setBuildTime(buildTime);


        return objectMapper.writeValueAsString(healthCheckInfoDto);
    }

    private String getUrl() {
        final String HTTP = "http://";
        final String SWAGER = "/swagger-ui.html";
        if (activeProfile.contains("prod")) {
            return HTTP + prodIp + ":" + serverPort + SWAGER;
        } else {
            return HTTP + "localhost" + ":" + serverPort + SWAGER;

        }
    }


}
