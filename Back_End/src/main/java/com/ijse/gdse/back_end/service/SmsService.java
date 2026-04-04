package com.ijse.gdse.back_end.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.PhoneNumber;
import org.springframework.stereotype.Service;

public interface SmsService {

    void sendAlert(String to, String city, String risk);

}
