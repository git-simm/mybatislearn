package simm.learning.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import simm.learning.web.config.Master1DataSourceConfig;
import simm.learning.web.config.Master2DataSourceConfig;

@SpringBootApplication(scanBasePackages = "simm.learning")
@PropertySource("classpath:config/db.properties")
@Import({Master1DataSourceConfig.class,Master2DataSourceConfig.class})
@EnableAspectJAutoProxy
public class WebApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
