package com.bbh.LedControllerApi.gateways.mqtt;

import com.bbh.LedControllerApi.forms.BreakoutEvent;
import com.bbh.LedControllerApi.services.breakoutService.BreakoutService;
import com.bbh.LedControllerApi.services.tttService.TicService;
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
public class TicMessageHandler {

    private static final Logger LOGGER = LogManager.getLogger(TicMessageHandler.class);

    @Autowired
    TicService ticService;

    @Autowired
    BreakoutService breakoutService;

    @Value("${mqtttopic}")
    private String topic;

    public void receiveMessage(Message<?> message) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//            TicEvent ticEvent = gson.fromJson((String) message.getPayload(), TicEvent.class);
//
//            if (ticEvent.getMqttClientId().equals("TttControllerApi")) {
//                LOGGER.info("New Message recived topic: " + topic + ": " + message.getPayload().toString()
//                        .replace("\r", "").replace("\n", ""));
//                ticService.handleTurn(ticEvent);
//            }

            breakoutService.handleTurn(gson.fromJson((String) message.getPayload(), BreakoutEvent.class));

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
