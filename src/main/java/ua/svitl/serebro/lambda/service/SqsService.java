package ua.svitl.serebro.lambda.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SqsService {

    @Value("${aws.messaging.sqs.queue.name}")
    private String queueName;

    private final AmazonSQS amazonSQS;

    public List<Message> receiveMessages() {
        String queueUrl = getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
        receiveMessageRequest.setQueueUrl(queueUrl);
        receiveMessageRequest.setMaxNumberOfMessages(1);

        ReceiveMessageResult receiveMessageResult = amazonSQS.receiveMessage(receiveMessageRequest);

        return receiveMessageResult.getMessages();
    }

    public void deleteMessage(Message message) {
        String queueUrl = getQueueUrl();

        amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
    }

    private String getQueueUrl() {
        return amazonSQS.getQueueUrl(queueName).getQueueUrl();
    }

}
