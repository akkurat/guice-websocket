package com.asafalima.websocket.services;

import javax.inject.Singleton;
import java.text.MessageFormat;
import java.util.Random;

/**
 * @author Asaf Alima
 */
@Singleton
public class MessagingService {

    public String getMessage(String user) {
        int i = new Random().nextInt(10000);
        return String.format("Personal Message for %s number is: %d", user, i) ;
    }

    public String getBroadcast() {
        int i = new Random().nextInt(10000);
        return "Broadcast. Number is: " + i;
    }
}
