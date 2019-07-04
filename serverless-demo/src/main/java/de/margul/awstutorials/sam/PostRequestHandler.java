package de.margul.awstutorials.sam;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class PostRequestHandler extends AbstractRequestHandler {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        DemoEntity entity = new DemoEntity(request.getBody());

        PutItemOutcome outcome = table
                .putItem(new Item().withPrimaryKey("id", entity.getId()).with("description", entity.getDescription()));

        System.out.println("Created entity with id " + entity.getId());

        return new APIGatewayProxyResponseEvent().withStatusCode(201);
    }

}
