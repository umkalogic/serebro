package com.example.cloudX.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

@Slf4j
public class RequestHandler extends SpringBootRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public Object handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        log.info("Event: {}", event);
        log.info("Context InvokedFunctionArn: {}", context.getInvokedFunctionArn());
        log.info("Context AwsRequestId: {}", context.getAwsRequestId());
        return super.handleRequest(event, context);
    }
}
