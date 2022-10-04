/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.template.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

/**
 *
 * @author Private
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.mycompany.web.template")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/tiles/tiles.xml"});
        tilesConfigurer.setCheckRefresh(true);
        return tilesConfigurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
        registry.viewResolver(viewResolver);
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());
        registry.addResourceHandler("/datetime/**").addResourceLocations("/resources/datetime/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());
        registry.addResourceHandler("/img/**").addResourceLocations("/resources/img/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());
        registry.addResourceHandler("/controller/**").addResourceLocations("/resources/controller/").setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic());

    }
}
