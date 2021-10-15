package com.Lee.bookstore_backend.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public InterceptorConfig() {
    }

    @Bean
    public SessionValidateInterceptor sessionValidateInterceptor() {
        return new SessionValidateInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.sessionValidateInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/Login")
            .excludePathPatterns("/static/**")
            .excludePathPatterns("/test")
            .excludePathPatterns("/static/bookImg/**")
            .excludePathPatterns("/getBooks")
            .excludePathPatterns("/searchBooks")
            .excludePathPatterns("/getBookById")
            .excludePathPatterns("/uploadImg")
            .excludePathPatterns("/checkInventory")
            .excludePathPatterns("/checkSession")
            .excludePathPatterns("/checkAdmin")
            .excludePathPatterns("/getAuth")
            .excludePathPatterns("/logout")
            .excludePathPatterns("/checkAuthority")
            .excludePathPatterns("/getUser")
            .excludePathPatterns("/getUserById")
            .excludePathPatterns("/register")
            .excludePathPatterns("/isDupName")
            .excludePathPatterns("/testDupName")
            .excludePathPatterns("/getHomePage")
            .excludePathPatterns("/book/getBooksForTest")
            .excludePathPatterns("/book/**")
            .excludePathPatterns("/getUserNow");
    }


    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", this.buildConfig());
        return new CorsFilter(source);
    }
}
