package com.example.cloudX.aws.lambda.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnsService {
    private static final String IMAGE_UPLOADED_NOTIFICATION = "Image uploaded notification";
    private static final String STRING_DATA_TYPE = "String";
    private static final String EXTENSION = "extension";

    @Value("${aws.messaging.sns.arn}")
    private String topicArn;

    private final AmazonSNS amazonSNS;

    public void publishNotificationMessage(Message message) {
        PublishRequest publishRequest = new PublishRequest(topicArn, message.getBody(), IMAGE_UPLOADED_NOTIFICATION);
        Map<String, MessageAttributeValue> messageAttributes = getExtensionAttribute(message);
        publishRequest.setMessageAttributes(messageAttributes);
        amazonSNS.publish(publishRequest);
    }

    private Map<String, MessageAttributeValue> getExtensionAttribute(Message message) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        MessageAttributeValue attributeValue = new MessageAttributeValue();
        attributeValue.setStringValue(StringUtils.substringBetween(message.getBody(), EXTENSION + ":", "\n"));
        attributeValue.setDataType(STRING_DATA_TYPE);
        messageAttributes.put(EXTENSION, attributeValue);
        return messageAttributes;
    }

    public void publishMessages(List<Message> receivedMessages) {
        receivedMessages.forEach(this::publishNotificationMessage);
    }
}
