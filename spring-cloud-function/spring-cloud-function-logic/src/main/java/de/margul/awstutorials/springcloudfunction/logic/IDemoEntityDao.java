package de.margul.awstutorials.springcloudfunction.logic;

import java.util.Optional;

public interface IDemoEntityDao {

    void createEntity(IDemoEntity entity);

    Optional<IDemoEntity> getEntity(String name);

    void updateEntity(IDemoEntity entity);

    void deleteEntity(String name);
}
