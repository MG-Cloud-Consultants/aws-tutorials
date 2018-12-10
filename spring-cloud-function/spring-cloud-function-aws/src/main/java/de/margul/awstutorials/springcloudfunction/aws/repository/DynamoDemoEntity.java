package de.margul.awstutorials.springcloudfunction.aws.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import de.margul.awstutorials.springcloudfunction.logic.IDemoEntity;

@DynamoDBTable(tableName = "DemoEntity")
public class DynamoDemoEntity implements IDemoEntity {

    private String name;

    @Override
    @DynamoDBHashKey
    public String getName() {

        return name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }
}
