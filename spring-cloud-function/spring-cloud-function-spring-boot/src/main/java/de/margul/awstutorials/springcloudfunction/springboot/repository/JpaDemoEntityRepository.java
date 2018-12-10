package de.margul.awstutorials.springcloudfunction.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDemoEntityRepository extends JpaRepository<JpaDemoEntity, String> {

}
