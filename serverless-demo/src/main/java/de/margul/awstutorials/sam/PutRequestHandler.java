package de.margul.awstutorials.sam;

import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class PutRequestHandler extends AbstractRequestHandler {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        DemoEntity entity = new DemoEntity(request.getBody());
        int id = Integer.valueOf(request.getPathParameters().get("id"));

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", id)
                .withUpdateExpression("set description = :d")
                .withValueMap(new ValueMap().withString(":d", entity.getDescription()));
        UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
        System.out.println("Updated entity with id " + id);

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
    }

}
