package com.asafalima.websocket.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Service
public class RegistrationListener {

    SimpMessagingTemplate templ;
    RegistrationListener(SimpMessagingTemplate templ)
    {
        this.templ = templ;
    }


    @EventListener
    public void handleConnectEvent(SessionConnectedEvent event)
    {
        System.out.println(event);
    }


}
