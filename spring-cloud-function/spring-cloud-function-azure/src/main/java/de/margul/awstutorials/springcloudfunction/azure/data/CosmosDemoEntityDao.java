package de.margul.awstutorials.springcloudfunction.azure.data;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntityDao;

@Repository
public class CosmosDemoEntityDao implements IDemoEntityDao {

    @Autowired
    private CosmosDemoEntityRepository repository;

    @Override
    public void createEntity(IDemoEntity entity) {

        repository.save((CosmosDemoEntity) entity);
    }

    @Override
    public Optional<IDemoEntity> getEntity(String name) {

        Optional<CosmosDemoEntity> entity = repository.findById(name);
        return Optional.of((IDemoEntity)entity.get());
    }

    @Override
    public void updateEntity(IDemoEntity entity) {

        repository.save((CosmosDemoEntity) entity);

    }

    @Override
    public void deleteEntity(String name) {

        repository.deleteById(name);
    }

}
