package ch.zhaw.card2brain.dto;

import lombok.Getter;
import lombok.Setter;

public class HealthCheckInfoDto {

    @Getter
    private final String APP_IS_RUNNING = "Card2Brain is running";

    @Getter
    @Setter
    private String swaggerUrl;


    @Getter
    @Setter
    private String appVersion;


    @Getter
    @Setter
    private String buildTime;


}
