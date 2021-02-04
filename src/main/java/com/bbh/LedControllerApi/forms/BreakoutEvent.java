package com.bbh.LedControllerApi.forms;

public class BreakoutEvent {
    private String[][] rgb;
    private boolean reset;

    public String[][] getRgb() {
        return this.rgb;
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