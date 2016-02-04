package org.mmonti.entitygraph.config;

import org.mmonti.entitygraph.repository.CustomGenericJpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by mmonti on 2/2/16.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.mmonti.entitygraph.repository", repositoryFactoryBeanClass = CustomGenericJpaRepositoryFactoryBean.class)
public class JPAConfig {

}
