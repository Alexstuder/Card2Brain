package ch.zhaw.card2brain.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The ApplicationKonsolOutput class is used to print out important information about the application
 * after initialization. This information includes the server port, active profiles, and links to the
 * Swagger documentation and H2 console (if applicable).
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller* @version 1.0
 * @since 28-01-2023
 */
@Component
public class ApplicationKonsolOutput {
    /*

    The env property is used to access the environment properties.
    */
    @Autowired
    private Environment env;

    /**
     * The after initialization of the class important information about the application will be printed to the console.
     */
    @PostConstruct
    public void afterInit() {
        String port = env.getProperty("server.port");
        boolean hasH2Database = Arrays.asList(env.getActiveProfiles()).contains("h2");

        String openApiInfo;
        String h2ConsoleInfo = "";

        // get Swager Links
        openApiInfo = String.join(System.getProperty("line.separator"), "http://localhost:" + port + "/v3/api-docs", "http://localhost:" + port + "/v3/api-docs.yaml -> yaml file is downloaded ->", "https://editor.swagger.io/", "http://localhost:" + port + "/swagger-ui.html \n");

        // Get h2 Links
        if (hasH2Database) {
            String h2ConnectionString = env.getProperty("spring.datasource.url");
            h2ConsoleInfo = "http://localhost:" + port + "/h2-console " + "" + "-> mit Generic H2 (Embedded), org.h2.Driver, " + h2ConnectionString + " und sa \n";
        }

        System.out.println("\n\nEnter in Browser:\nhttp://localhost:" + port + " \n" + " \n" + openApiInfo + "\n" + h2ConsoleInfo + "\n" + "Active Profiles: " + Arrays.toString(env.getActiveProfiles()) + "\n");
    }

}
