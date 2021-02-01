package com.wilderman.reviewer.dto.SmsResponseHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wilderman.reviewer.db.primary.entities.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MessageObject {

    @JsonIgnore
    @Value("${aws.sns.number}")
    private String snsNumber;


    private String originationNumber;
    private String destinationNumber;
    private String messageKeyword;
    private String messageBody;
    private String inboundMessageId;
    private String previousPublishedMessageId;

    private static String snsPhoneNumber;

    @PostConstruct
    public void init() {
        snsPhoneNumber = this.snsNumber;
    }

    public MessageObject() {
        destinationNumber = snsPhoneNumber;
        messageKeyword = "keyword_123";
        inboundMessageId = "";
        previousPublishedMessageId = "";
    }

    public MessageObject(Patient patient, Integer rating) {
        this();
        setOriginationNumber(patient.getPhone());
        setMessageBody(rating + "");
    }

    public String getOriginationNumber() {
        return originationNumber;
    }

    public void setOriginationNumber(String originationNumber) {
        this.originationNumber = originationNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
