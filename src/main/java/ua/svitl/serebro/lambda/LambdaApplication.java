package ua.svitl.serebro.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import ua.svitl.serebro.lambda.service.RoutingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class LambdaApplication {

    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> messages(RoutingService routingService) {
        return event -> {
            routingService.routeMessages();

            APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
            apiGatewayProxyResponseEvent.setStatusCode(200);
            apiGatewayProxyResponseEvent.setBody("Messages successfully sent");

            return apiGatewayProxyResponseEvent;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }

}
