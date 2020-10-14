package com.bbh.LedControllerApi.forms;

import org.springframework.stereotype.Component;

@Component
public class BreakoutEvent {
    private String[][] rgb;
    private boolean reset;

    public String[][] getRgb() {
        return rgb;
    }

    public void setRgb(String[][] rgb) {
        this.rgb = rgb;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}