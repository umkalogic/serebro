package ua.svitl.serebro.lambda.service;

import com.amazonaws.services.sqs.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class RoutingService {

    @Value("1000")
    private Long batchTimeout;

    @Value("3")
    private Long batchSize;

    private final SnsService snsService;

    private final SqsService sqsService;

    public void routeMessages() {
        List<Message> receivedMessages = new ArrayList<>();

        long startTime = currentTimeMillis();

        while (receivedMessages.size() < batchSize && (currentTimeMillis() - startTime) < batchTimeout) {
            List<Message> messages = sqsService.receiveMessages();
            messages.forEach(message -> {
                receivedMessages.add(message);

                sqsService.deleteMessage(message);
            });
        }

        snsService.publishMessages(receivedMessages);
    }
}
