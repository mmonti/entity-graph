package org.mmonti.entitygraph.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by mmonti on 2/2/16.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module().disable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .modulesToInstall(hibernate4Module())
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        return new MappingJackson2HttpMessageConverter(jackson2ObjectMapperBuilder().build());
    }
}

