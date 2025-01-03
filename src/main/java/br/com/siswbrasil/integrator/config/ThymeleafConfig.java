package br.com.siswbrasil.integrator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

@Configuration
public class ThymeleafConfig implements WebMvcConfigurer {

    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;

    @PostConstruct
    public void init() {
        thymeleafViewResolver.addStaticVariable("applicationName", "integrator");
    }
}