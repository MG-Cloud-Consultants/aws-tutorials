package de.margul.awstutorials.springcloudfunction.springboot.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;
import de.margul.awstutorials.springcloudfunction.springboot.repository.JpaDemoEntityDeserializer;

@Configuration
@EntityScan(basePackages = { "de.margul.awstutorials.springcloudfunction.springboot.repository" })
@EnableJpaRepositories(basePackages = "de.margul.awstutorials.springcloudfunction.springboot.repository")
public class JpaConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
        return dataSource;
    }

    @Bean
    public Module jpaDemoEntityDeserializer() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new JpaDemoEntityDeserializer());
        return module;
    }
}
