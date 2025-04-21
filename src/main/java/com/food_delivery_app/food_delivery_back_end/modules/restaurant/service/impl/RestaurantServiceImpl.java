package com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.impl;

import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.Account;
import com.food_delivery_app.food_delivery_back_end.modules.auth.entity.AccountRole;
import com.food_delivery_app.food_delivery_back_end.modules.auth.repository.AccountRepository;
import com.food_delivery_app.food_delivery_back_end.modules.dish.dto.DishResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.entity.Restaurant;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.repostitory.RestaurantRepository;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantDetailResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.dto.RestaurantResponseDto;
import com.food_delivery_app.food_delivery_back_end.modules.restaurant.service.RestaurantService;
import com.food_delivery_app.food_delivery_back_end.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final AccountRepository accountRepository;
    private final UploadUtils uploadUtils;

    @Override
    @Cacheable(value = "restaurantList", key = "{#keyword, #page, #limit}")
    public Page<RestaurantResponseDto> getAllRestaurants(String keyword, int page, int limit) {
        log.info("CACHE MISS - Lấy danh sách nhà hàng từ database: keyword={}, page={}, limit={}", keyword, page, limit);
        Pageable pageable = PageRequest.of(page, limit);
        return restaurantRepository.findRestaurantsByActiveRole(keyword, pageable);
    }

    @Override
    @CacheEvict(value = {"restaurantList", "restaurantDetail"}, allEntries = true)
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        log.info("XÓA CACHE - Cập nhật thông tin nhà hàng và xóa cache: id={}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.setName(restaurantDto.getName());
        restaurant.setAddress(restaurantDto.getAddress());
        restaurant.setDescription(restaurantDto.getDescription());
        restaurantRepository.save(restaurant);

        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    @Cacheable(value = "restaurantDetail", key = "#id")
    public RestaurantDetailResponseDto getRestaurant(Long id) {
        log.info("CACHE MISS - Lấy chi tiết nhà hàng từ database: id={}", id);
        Restaurant restaurant = restaurantRepository.findRestaurantById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        RestaurantDetailResponseDto restaurantDetailResponseDto = modelMapper.map(restaurant, RestaurantDetailResponseDto.class);
        restaurantDetailResponseDto.setPhoneNumber(restaurant.getAccount().getPhoneNumber());
        restaurantDetailResponseDto.setEmail(restaurant.getAccount().getEmail());
        restaurantDetailResponseDto.setLongitude(restaurant.getLongitude());
        restaurantDetailResponseDto.setLatitude(restaurant.getLatitude());
        List<DishResponseDto> dishResponseDtos = restaurant.getDishes().stream()
                .map(dish -> DishResponseDto.builder()
                        .id(dish.getId())
                        .name(dish.getName())
                        .description(dish.getDescription())
                        .thumbnail(dish.getThumbnail())
                        .category(dish.getCategory().getName())
                        .price(dish.getPrice())
                        .build())
                .collect(Collectors.toList());
        restaurantDetailResponseDto.setDishes(dishResponseDtos);

        return restaurantDetailResponseDto;
    }

    @Override
    @CacheEvict(value = {"restaurantList", "restaurantDetail"}, allEntries = true)
    public void deleteRestaurant(Long id) {
        log.info("XÓA CACHE - Xóa nhà hàng và xóa cache: id={}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        Account account = accountRepository.findAccountByRestaurant(restaurant)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Set<AccountRole> accountRoles = account.getAccountRoles();
        for(AccountRole accountRole : accountRoles){
            if(accountRole.getRoleType().name().equals("ROLE_RESTAURANT")){
                System.out.println(accountRole.getRoleType().name());
                accountRole.setActive(false);
                break;
            }
        }
        account.setAccountRoles(accountRoles);
        accountRepository.save(account);
    }

    @Override
    @CacheEvict(value = {"restaurantList", "restaurantDetail"}, allEntries = true)
    public String uploadImage(Long id, MultipartFile file) throws IOException {
        log.info("XÓA CACHE - Cập nhật ảnh nhà hàng và xóa cache: id={}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        String uploadUrl = uploadUtils.uploadFile(file);
        restaurant.setPhotoUrl(uploadUrl);
        restaurantRepository.save(restaurant);

        return uploadUrl;
    }
}
