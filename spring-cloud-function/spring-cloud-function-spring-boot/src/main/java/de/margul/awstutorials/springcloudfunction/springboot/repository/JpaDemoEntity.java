package de.margul.awstutorials.springcloudfunction.springboot.repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@Entity
@Table(name = "Person")
public class JpaDemoEntity implements IDemoEntity {

    @Id
    private String name;

    @Override
    public String getName() {

        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }
}
