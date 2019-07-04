package de.margul.awstutorials.sam;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class DeleteRequestHandler extends AbstractRequestHandler {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        int id = Integer.valueOf(request.getPathParameters().get("id"));

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec().withPrimaryKey(new PrimaryKey("id", id));
        table.deleteItem(deleteItemSpec);
        System.out.println("Deleted entity with id " + id);

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
    }
}
