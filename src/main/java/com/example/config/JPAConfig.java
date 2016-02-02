package com.example.config;

import com.example.repository.CustomGenericJpaRepositoryFactoryBean;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by mmonti on 2/2/16.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.repository", repositoryFactoryBeanClass = CustomGenericJpaRepositoryFactoryBean.class)
public class JPAConfig {

    @Bean
    public Hibernate4Module hibernate4Module() {
        final Hibernate4Module hibernate4Module = new Hibernate4Module();
        hibernate4Module.enable(Hibernate4Module.Feature.FORCE_LAZY_LOADING);

        return hibernate4Module;
    }
}
