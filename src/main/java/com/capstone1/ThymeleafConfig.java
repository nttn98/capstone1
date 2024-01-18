package com.capstone1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean
    public ClassLoaderTemplateResolver seconLoaderTemplateResolver() {
        ClassLoaderTemplateResolver seconLoaderTemplateResolver = new ClassLoaderTemplateResolver();
        seconLoaderTemplateResolver.setSuffix(".html");
        seconLoaderTemplateResolver.setTemplateMode(TemplateMode.HTML);
        // seconLoaderTemplateResolver.setCharacterEncoding("UTF-8");
        seconLoaderTemplateResolver.setOrder(1);
        seconLoaderTemplateResolver.setCheckExistence(true);
        return seconLoaderTemplateResolver;

    }
}
