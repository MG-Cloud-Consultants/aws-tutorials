package de.margul.awstutorials.springcloudfunction.azure.data;

import org.springframework.stereotype.Repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.DocumentDbRepository;

@Repository
public interface CosmosDemoEntityRepository extends DocumentDbRepository<CosmosDemoEntity, String> {

}
