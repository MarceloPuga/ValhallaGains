package com.ValhallaGains.ValhallaGains;

import com.ValhallaGains.ValhallaGains.dtos.NewClientDTO;
import com.ValhallaGains.ValhallaGains.models.*;
import com.ValhallaGains.ValhallaGains.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ValhallaGainsApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(ValhallaGainsApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, CategoryRepository categoryRepository,
									  DirectionRepository directionRepository, PurchaseRepository purchaseRepository,
									  ProductRepository productRepository, PurchaseProductRepository purchaseProductRepository) {
		return args -> {

			Category usable = new Category("usable");
			Category consumable = new Category("consumable");
			categoryRepository.save(usable);
			categoryRepository.save(consumable);
			Product trainingWeight = new Product("training weight","to gain muscle tone",4000,12L,"https://res.cloudinary.com/dfbffoqjc/image/upload/v1706547634/imagenes%20ValhallaGains/hsmittyeulqgezd0atog.jpg");
			Product carboEnergy = new Product("Carbo-Energy","Carbo Energy ENA SPORT is an excellent combination of 100% carbohydrates, a balanced source of energy for physical efforts.\"",12000,32L,"https://res.cloudinary.com/dtkzk0ucn/image/upload/v1707148184/imagenes/kdjna59gnmbeefduqrcn.jpg");
			Product gymMat = new Product("Gym mat","1×0.50×0.04m high-density mat with non-absorbent waterproof cover, with closure. Washable.",6000,1L,"https://res.cloudinary.com/dtkzk0ucn/image/upload/v1707148185/imagenes/srxaqyjv24npple5nh7g.jpg");
			trainingWeight.setCategory(usable);
			carboEnergy.setCategory(consumable);
			gymMat.setCategory(consumable);
			productRepository.save(trainingWeight);
			productRepository.save(carboEnergy);
			productRepository.save(gymMat);
			Client admin = new Client();
			Direction adminDirection = new Direction("argelia","tiramisu",123,123,"mandinga","av.siempre viva");
			admin.setRole("admin");
			admin.setDirections(adminDirection);
			admin.setName("admin");
			admin.setEmail("admin@admin.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setLastName("vikingo");
			admin.setAccessCode("admin123");
			directionRepository.save(adminDirection);
			clientRepository.save(admin);

		};
	}

}
