package com.wilderman.reviewer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.dto.SmsResponseHandler.MessageObject;
import com.wilderman.reviewer.dto.SmsResponseHandler.SmsResponseHandlerObject;
import com.wilderman.reviewer.dto.SmsResponseHandler.SnsObject;

import java.util.ArrayList;
import java.util.List;

public class SmsResponseHandlerInput {
    /*
        [
          {
            "Sns": {
              "Message": {
                "originationNumber": "+16479661556",
                "destinationNumber": "+15878415576",
                "messageKeyword": "keyword_381541571067",
                "messageBody": "5",
                "inboundMessageId": "d2ee59f1-4784-59e7-94e5-01b58f00bc8b",
                "previousPublishedMessageId": "3621d200-ea7b-5331-9050-8a44223ef5b1"
              }
            }
          }
        ]
     */

    public SmsResponseHandlerInput(Patient patient, Integer rating) throws Exception {
        if (rating == null) {
            throw new Exception("Rating cannot be null");
        }
        MessageObject message = new MessageObject(patient, rating);
        SnsObject snsObject = new SnsObject(message);
        SmsResponseHandlerObject obj = new SmsResponseHandlerObject(snsObject);
        records.add(obj);
    }

    @JsonProperty("Records")
    List<SmsResponseHandlerObject> records = new ArrayList<>();

    public List<SmsResponseHandlerObject> getRecords() {
        return records;
    }

    public void setRecords(List<SmsResponseHandlerObject> records) {
        this.records = records;
    }

    public boolean isValid() {
        return this.getRecords().get(0).getSns().getMessage() != null;
    }
}
