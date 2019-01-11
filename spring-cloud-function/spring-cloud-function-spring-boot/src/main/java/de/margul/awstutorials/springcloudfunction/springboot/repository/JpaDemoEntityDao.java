package de.margul.awstutorials.springcloudfunction.springboot.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntityDao;

@Repository
public class JpaDemoEntityDao implements IDemoEntityDao {

    @Autowired
    private JpaDemoEntityRepository repository;

    @Override
    public void createEntity(IDemoEntity entity) {

        repository.save((JpaDemoEntity) entity);
    }

    @Override
    public Optional<IDemoEntity> getEntity(String name) {

        Optional<JpaDemoEntity> entity = repository.findById(name);
        return Optional.of((IDemoEntity) entity.get());
    }

    @Override
    public void updateEntity(IDemoEntity entity) {

        repository.save((JpaDemoEntity) entity);

    }

    @Override
    public void deleteEntity(String name) {

        repository.deleteById(name);
    }

}
