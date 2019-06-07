package com.hairbooking.hairsalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
public class HairsalonApplication {
	@Bean
	public Docket studentAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("HairSalonBooking")
				.apiInfo(new ApiInfoBuilder().title("HairSalonBooking").description("Hair Salon Booking").build())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.hairbooking.hairsalon"))
				.paths(PathSelectors.regex("/api.*"))
				.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(HairsalonApplication.class, args);
	}

}
