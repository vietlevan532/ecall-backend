package vn.ecall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import vn.ecall.config.ApplicationProperties;
import vn.ecall.config.EmailProperties;

import javax.servlet.Filter;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class, EmailProperties.class})
public class EcallApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcallApplication.class, args);
	}

	@Bean
	public Filter filter() {
		return new ShallowEtagHeaderFilter();
	}
}
