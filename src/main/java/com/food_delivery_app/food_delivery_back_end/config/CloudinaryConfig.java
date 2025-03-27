package com.food_delivery_app.food_delivery_back_end.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyzx3woh4",
                "api_key", "226513877685445",
                "api_secret", "66uviNWvbiPIgfyjt2cVHC9nzBQ",
                "secure", true
        ));
    }
}
