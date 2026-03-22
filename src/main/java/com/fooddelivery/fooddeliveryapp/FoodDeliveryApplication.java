package com.fooddelivery.fooddeliveryapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodDeliveryApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing() // không báo lỗi nếu không có file
				.load();

		// Gán biến môi trường (chỉ cần nếu bạn muốn Spring Boot nhận qua ${})
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(FoodDeliveryApplication.class, args);
	}

}
