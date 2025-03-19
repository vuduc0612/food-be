//package com.food_delivery_app.food_delivery_back_end.data;
//
//import com.food_delivery_app.food_delivery_back_end.constant.RoleType;
//import com.food_delivery_app.food_delivery_back_end.modules.auth.dto.RegisterDto;
//
//import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
//import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRoleRepository;
//import com.food_delivery_app.food_delivery_back_end.modules.auth.service.AuthService;
//import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
//import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
//import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
//import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
//import com.food_delivery_app.food_delivery_back_end.modules.user.entity.User;
//import com.food_delivery_app.food_delivery_back_end.modules.user.repository.UserRepository;
//import com.github.javafaker.Faker;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Locale;
//
//@Component
//@RequiredArgsConstructor
//public class DataSeeder implements CommandLineRunner {
//    private final AccountRepository accountRepository;
//    private final UserRepository userRepository;
//    private final RestaurantRepository restaurantRepository;
//    private final AccountRoleRepository accountRoleRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final DishRepository dishRepository;
//    private final AuthService authService;
//
//
//
//    @Override
//    public void run(String... args) {
//        Faker faker = new Faker(new Locale("vi"));
//
//        // Tạo 10 Users
//        for (int i = 0; i < 1000; i++) {
//            String email = faker.internet().emailAddress();
//            String phone = faker.phoneNumber().cellPhone();
//
//            RegisterDto registerDto = new RegisterDto(email, phone, "password123");
//            authService.register(registerDto, RoleType.ROLE_USER);
//
//        }
//        List<User> users = userRepository.findAll();
//        for(User user : users){
//            user.setUsername(faker.name().fullName());
//            user.setAddress(faker.address().fullAddress());
//            System.out.println(user);
//            userRepository.save(user);
//        }
//
//        System.out.println("Generated 100 fake users successfully!");
//
//         //Tạo 10 Restaurants
//        for (int i = 0; i < 100; i++) {
//            String email = faker.internet().emailAddress();
//            String phone = faker.phoneNumber().cellPhone();
//
//            RegisterDto registerDto = new RegisterDto(email, phone, "password123");
//            authService.register(registerDto, RoleType.ROLE_RESTAURANT);
//
//            List<Restaurant> restaurants = restaurantRepository.findAll();
//            for(Restaurant restaurant : restaurants){
//                restaurant.setName(faker.company().name());
//                restaurant.setAddress(faker.address().fullAddress());
//                restaurantRepository.save(restaurant);
//            }
//        }
//
//        System.out.println("Generated 1000 fake restaurants successfully!");
//
//        List<Restaurant> restaurants = restaurantRepository.findAll();
//        for(Restaurant restaurant: restaurants){
//            Long idRestaurant = restaurant.getId();
//            for(int i = 0; i < 10; i++){
//                String dishName = faker.food().ingredient();
//                String dishDescription = faker.lorem().sentence();
//                Double dishPrice = faker.number().randomDouble(2, 1, 100);
//                Dish dish = new Dish();
//                dish.setName(dishName);
//                dish.setDescription(dishDescription);
//                dish.setPrice(dishPrice);
//                dish.setRestaurant(restaurant);
//                dishRepository.save(dish);
//
//            }
//            System.out.println("Generated 20 fake dishes successfully!" + idRestaurant);
//        }
//    }
//}
