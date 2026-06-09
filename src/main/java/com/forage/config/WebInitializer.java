package com.forage.config;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration.Dynamic;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { RootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();

        return new Filter[] { encodingFilter, hiddenHttpMethodFilter };
    }

    @Override
    protected void customizeRegistration(Dynamic registration) {
        // Correction ici : On utilise l'import jakarta.servlet.MultipartConfigElement
        String location = System.getProperty("java.io.tmpdir");
        long maxFileSize = 20971520L; // 20MB
        long maxRequestSize = 41943040L; // 40MB
        int fileSizeThreshold = 0;
        
        registration.setMultipartConfig(new MultipartConfigElement(
                location, maxFileSize, maxRequestSize, fileSizeThreshold));
    }
}