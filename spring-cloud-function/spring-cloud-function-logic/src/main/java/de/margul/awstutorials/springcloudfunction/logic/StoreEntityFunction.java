package de.margul.awstutorials.springcloudfunction.logic;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class StoreEntityFunction implements Consumer<Message<IDemoEntity>> {

    @Autowired
    private CrudRepository repository;

    @Override
    public void accept(Message<IDemoEntity> t) {

        System.out.println("Stored entity with name " + t.getPayload().getName());
        repository.save(t.getPayload());
        return;
    }
}