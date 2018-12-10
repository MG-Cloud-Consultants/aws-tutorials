package de.margul.awstutorials.springcloudfunction.aws.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamoDemoEntityRepository extends CrudRepository<DynamoDemoEntity, String> {

}
