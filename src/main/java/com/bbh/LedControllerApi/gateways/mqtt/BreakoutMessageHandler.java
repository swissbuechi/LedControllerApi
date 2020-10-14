package com.bbh.LedControllerApi.gateways.mqtt;

import com.bbh.LedControllerApi.forms.BreakoutEvent;
import com.bbh.LedControllerApi.services.breakoutService.BreakoutService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class BreakoutMessageHandler {

    private static final Logger LOGGER = LogManager.getLogger(BreakoutMessageHandler.class);

    @Autowired
    private BreakoutService breakoutService;

    @Value("${mqtttopic}")
    private String topic;

    public void receiveMessage(Message<?> message) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            BreakoutEvent breakoutEvent = gson.fromJson((String) message.getPayload(), BreakoutEvent.class);
            LOGGER.info("New Message recived topic: " + topic + ": " + message.getPayload().toString()
                    .replace("\r", "").replace("\n", ""));
            breakoutService.handleTurn(breakoutEvent);
        } catch (JsonSyntaxException e) {
            LOGGER.error("New Message recived topic: " + topic + ": Maleformed JSON: " + message.getPayload().toString()
                    .replace("\r", "").replace("\n", ""));
            e.printStackTrace();
        } catch (NullPointerException e) {
            LOGGER.error("New Message recived topic: " + topic + ": Nullpointer Exception in JSON: " + message.getPayload().toString()
                    .replace("\r", "").replace("\n", ""));
        }
    }
}
