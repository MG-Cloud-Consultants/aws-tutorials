package de.margul.awstutorials.rds.iam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
import org.springframework.cloud.aws.jdbc.datasource.DataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.margul.awstutorials.rds.iam.repository.IamTomcatJdbcDataSourceFactory;

@Configuration
@EntityScan(basePackages = { "de.margul.awstutorials.rds.iam.repository" })
@EnableJpaRepositories(basePackages = { "de.margul.awstutorials.rds.iam.repository" })
@EnableRdsInstance(dbInstanceIdentifier = "demo-instance", username = "demouser", password = "", databaseName = "demodb")
public class DbConfiguration {

    @Value("${cloud.aws.region}")
    private String region;

    @Bean
    public RdsInstanceConfigurer instanceConfigurer() {

        return new RdsInstanceConfigurer() {
            @Override
            public DataSourceFactory getDataSourceFactory() {
                IamTomcatJdbcDataSourceFactory dataSourceFactory = new IamTomcatJdbcDataSourceFactory();
                dataSourceFactory.setRegion(region);

                return dataSourceFactory;
            }
        };
    }
}