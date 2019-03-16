package de.margul.awstutorials.springcloudfunction.azure.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;
import de.margul.awstutorials.springcloudfunction.logic.IDemoEntityDao;

@Repository
public class CosmosDemoEntityDao implements IDemoEntityDao {

    //@Autowired
    private CosmosDemoEntityRepository repository;

    @Override
    public void createEntity(IDemoEntity entity) {

        System.out.println("Created entity " + entity);
        repository.save((CosmosDemoEntity) entity);
    }

    @Override
    public Optional<IDemoEntity> getEntity(String name) {

        CosmosDemoEntity entity = new CosmosDemoEntity();
        entity.setName(name);
        entity.setDescription("Description");
        //Optional<CosmosDemoEntity> entity = repository.findById(name);
        return Optional.of((IDemoEntity) entity);
    }

    @Override
    public void updateEntity(IDemoEntity entity) {

        System.out.println("Updated entity " + entity);
        //repository.save((CosmosDemoEntity) entity);

    }

    @Override
    public void deleteEntity(String name) {

        System.out.println("Deleted entity with name " + name);
        repository.deleteById(name);
    }

}
