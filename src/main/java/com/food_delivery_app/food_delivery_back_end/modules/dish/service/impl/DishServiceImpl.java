package com.food_delivery_app.food_delivery_back_end.modules.dish.service.impl;

import com.food_delivery_app.food_delivery_back_end.constant.DishStatusType;
import com.food_delivery_app.food_delivery_back_end.exception.DataNotFoundException;
import com.food_delivery_app.food_delivery_back_end.modules.category.entity.Category;
import com.food_delivery_app.food_delivery_back_end.modules.category.repository.CategoryRepository;
import com.food_delivery_app.food_delivery_back_end.modules.category.service.CategoryService;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishStatusRequestDto;
import com.food_delivery_app.food_delivery_back_end.modules.dish.entity.Dish;
import com.food_delivery_app.food_delivery_back_end.modules.dish.repository.DishRepository;
import com.food_delivery_app.food_delivery_back_end.modules.dish.service.DishService;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DishServiceImpl implements DishService {
    private final ModelMapper modelMapper;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "restaurantDishes", key = "{#page, #limit}")
    public Page<DishResponseDto> getAllDishes(int page, int limit) {
        log.info("CACHE MISS - Lấy tất cả món ăn từ database: page={}, limit={}", page, limit);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Dish> dishPage = dishRepository.findAll(pageable);
        dishPage.map(
                dish -> {
                   //dish.setIsAvailable(DishStatusType.AVAILABLE);
                   //dishRepository.save(dish);
                   return dish;
                }
        );
        return dishPage.map(dish -> modelMapper.map(dish, DishResponseDto.class));
    }

    @Override
    @Cacheable(value = "restaurantDishes", key = "{#restaurantId, #categoryId, #keyword, #page, #limit}")
    public Page<DishResponseDto> getAllDishByRestaurant(Long restaurantId, Long categoryId, String keyword, int page, int limit) {
        log.info("CACHE MISS - Lấy danh sách món ăn theo nhà hàng từ database: restaurantId={}, categoryId={}, keyword={}, page={}, limit={}", 
                restaurantId, categoryId, keyword, page, limit);
                
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Page<Dish> dishPage = dishRepository.findDishes(restaurant, categoryId, keyword, pageable);

        return dishPage.map(dish -> {
            DishResponseDto dishResponseDto = modelMapper.map(dish, DishResponseDto.class);
            dishResponseDto.setCategory(dish.getCategory().getName());
            return dishResponseDto;

        });
    }

    @Override
    @Cacheable(value = "categoryDishes", key = "{#categoryId, #restaurantId, #page, #limit}")
    public Page<DishResponseDto> getAllDishByCategory(Long categoryId, Long restaurantId, int page, int limit) {
        log.info("CACHE MISS - Lấy danh sách món ăn theo danh mục từ database: categoryId={}, restaurantId={}, page={}, limit={}", 
                categoryId, restaurantId, page, limit);
                
        Pageable pageable = PageRequest.of(page, limit);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if(category.getRestaurant().getId() != restaurantId){
            throw new RuntimeException("Category not found in restaurant with id: " + restaurantId);
        }
        Page<Dish> dishPage = dishRepository.findByRestaurantAndCategory(restaurant, category, pageable);
        return dishPage.map(dish -> modelMapper.map(dish, DishResponseDto.class));
    }


    @Override
    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        return DishResponseDto.builder()
                .category(dish.getCategory().getName())
                .description(dish.getDescription())
                .id(dish.getId())
                .name(dish.getName())
                .thumbnail(dish.getThumbnail())
                .price(dish.getPrice())
                .build();
    }


    @Override
    @CacheEvict(value = {"restaurantDishes", "categoryDishes"}, allEntries = true)
    public DishResponseDto createDish(DishRequestDto dishRequestDto, Long restaurantId) {
        log.info("XÓA CACHE - Tạo món ăn mới và xóa cache danh sách món ăn");
        
        Dish dish = Dish.builder()
                .name(dishRequestDto.getName())
                .price(dishRequestDto.getPrice())
                .description(dishRequestDto.getDescription())
                .thumbnail(dishRequestDto.getThumbnail())
                .build();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found"));
        dish.setRestaurant(restaurant);

        Category category;
        if(categoryRepository.existsByName(dishRequestDto.getCategoryName())){
            category = categoryRepository.findByName(dishRequestDto.getCategoryName())
                    .orElseThrow(() -> new DataNotFoundException("Category not found"));
        } else {
            Category newCategory = Category.builder()
                    .name(dishRequestDto.getCategoryName())
                    .build();
            newCategory.setRestaurant(restaurant);
            category = categoryRepository.save(newCategory);
        }
        dish.setCategory(category);
        dish.setIsAvailable(DishStatusType.AVAILABLE);

        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }

    @Override
    @CacheEvict(value = {"restaurantDishes", "categoryDishes"}, allEntries = true)
    public DishResponseDto updateDish(Long id, Long restaurantId, DishRequestDto dishRequestDto) {
        log.info("XÓA CACHE - Cập nhật món ăn và xóa cache danh sách món ăn: id={}, restaurantId={}", id, restaurantId);
        
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DataNotFoundException("Restaurant not found"));
        System.out.println(dishRequestDto.getPrice() + " " + dishRequestDto.getName() + " " + dishRequestDto.getDescription()
                + " " + dishRequestDto.getThumbnail() + " " + dishRequestDto.getCategoryName());
        dish.setName(dishRequestDto.getName());
        dish.setPrice(dishRequestDto.getPrice());
        dish.setDescription(dishRequestDto.getDescription());
        dish.setThumbnail(dishRequestDto.getThumbnail());

        Category category;
        if(categoryRepository.existsByName(dishRequestDto.getCategoryName())){
            category = categoryRepository.findByName(dishRequestDto.getCategoryName())
                    .orElseThrow(() -> new DataNotFoundException("Category not found"));
        } else {
            Category newCategory = Category.builder()
                    .name(dishRequestDto.getCategoryName())
                    .build();
            newCategory.setRestaurant(restaurant);
            category = categoryRepository.save(newCategory);
        }
        dish.setCategory(category);
        dishRepository.save(dish);
        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }

    @Override
    @CacheEvict(value = {"restaurantDishes", "categoryDishes"}, allEntries = true)
    public DishResponseDto updateStatusDish(Long id, Long restaurantId, DishStatusRequestDto dishStatus) {
        log.info("XÓA CACHE - Cập nhật trạng thái món ăn và xóa cache danh sách món ăn: id={}, restaurantId={}", id, restaurantId);
        
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        dish.setIsAvailable(DishStatusType.valueOf(dishStatus.getStatus().toUpperCase()));
        return modelMapper.map(dishRepository.save(dish), DishResponseDto.class);
    }


    @Override
    @CacheEvict(value = {"restaurantDishes", "categoryDishes"}, allEntries = true)
    public void deleteDish(Long id, Long restaurantId) {
        log.info("XÓA CACHE - Xóa món ăn và xóa cache danh sách món ăn: id={}, restaurantId={}", id, restaurantId);
        
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found"));
        if(dish.getRestaurant().getId() != restaurantId){
            throw new DataNotFoundException("Dish not found in restaurant with id: " + restaurantId);
        }
        dishRepository.delete(dish);
    }
}
