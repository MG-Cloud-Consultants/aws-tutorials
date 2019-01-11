package de.margul.awstutorials.springcloudfunction.aws.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.margul.awstutorials.springcloudfunction.aws.repository.DynamoDemoEntityDeserializer;
import de.margul.awstutorials.springcloudfunction.aws.repository.DynamoDemoEntityRepository;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = DynamoDemoEntityRepository.class)
public class DynamoDBConfig {

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
        return new DynamoDBMapper(amazonDynamoDB, config);
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withRegion(Regions.fromName(System.getenv("AWS_DEFAULT_REGION")))
                .build();
    }

    @Bean
    public Module dynamoDemoEntityDeserializer() {

        SimpleModule module = new SimpleModule();
        module.addDeserializer(IDemoEntity.class, new DynamoDemoEntityDeserializer());
        return module;
    }
}
