package org.spring.springboot.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by betty on 19/05/2020.
 */
@Configurable
public class ApiSecurityConfig implements WebMvcConfigurer {

    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // TODO 这里跨域最好配置域名
                .allowedOrigins("*")
                .maxAge(3600)
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "OPTIONS");
    }

}
