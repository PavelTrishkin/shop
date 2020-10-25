package ru.gb.trishkin.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gb.trishkin.shop.config.SellIntegrationConfig;

@SpringBootApplication
public class ShopApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ShopApplication.class, args);
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		System.out.println("Encode password 'admin': " + encoder.encode("admin"));
		System.out.println("Encode password 'pass': " + encoder.encode("pass"));

	}

}
