package com.example.cloudX.aws.lambda.service;

import com.amazonaws.services.sqs.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

@EnableScheduling
@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {

    @Value("${aws.routing.batch.timeout.ms}")
    private Long batchTimeout;
    @Value("${aws.routing.batch.size}")
    private Long batchSize;

    private final SnsService snsService;
    private final SqsService sqsService;

    public void routeMessages() {
        log.info("Starting receiving messages");
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
        log.info("Ending receiving messages");
    }
}
