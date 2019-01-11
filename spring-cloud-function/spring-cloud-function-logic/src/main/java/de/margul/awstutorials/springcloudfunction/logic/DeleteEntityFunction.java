package de.margul.awstutorials.springcloudfunction.logic;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DeleteEntityFunction implements Consumer<Message<Void>> {

    @Autowired
    private IDemoEntityDao dao;

    @Override
    public void accept(Message<Void> m) {

        String name = (String) m.getHeaders().get("name");
        dao.deleteEntity(name);
        System.out.println("Deleted entity with name " + name);
    }
}
