package com.forage.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.forage") // Cherche les classes @Controller ici
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/"); // Dossier des pages
        resolver.setSuffix(".jsp");            // Extension
        resolver.setOrder(1);
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // Permet au conteneur de servir les ressources statiques non gérées par Spring
        configurer.enable();
    }

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     // Mapping des ressources statiques (css/js/images)
    //     registry.addResourceHandler("/resources/**")
    //             .addResourceLocations("/resources/")
    //             .setCachePeriod(3600);
    // }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL dans le navigateur --> Emplacement physique dans 'webapp'
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        // Forcer UTF-8 pour les réponses textuelles
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
