package de.margul.awstutorials.sam;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class GetRequestHandler extends AbstractRequestHandler {

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        int id = Integer.valueOf(request.getPathParameters().get("id"));

        Item result = table.getItem("id", id);

        DemoEntity entity = new DemoEntity(result.getInt("id"), result.getString("description"));

        return new APIGatewayProxyResponseEvent().withBody(entity.toString()).withStatusCode(200);
    }

}
