package com.edsom.EraPay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.edsom.EraPay.*",

})
public class EraPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EraPayApplication.class, args);
	}

}
