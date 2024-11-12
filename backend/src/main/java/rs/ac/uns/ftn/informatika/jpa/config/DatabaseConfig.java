package rs.ac.uns.ftn.informatika.jpa.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DatabaseConfig implements ApplicationListener<ApplicationReadyEvent>  {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            // Load the SQL script from the classpath
            ClassPathResource resource = new ClassPathResource("data.sql"); // data.sql is inside src/main/resources
            String sqlScript = new String(Files.readAllBytes(Paths.get(resource.getURI())));
            jdbcTemplate.execute(sqlScript);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


