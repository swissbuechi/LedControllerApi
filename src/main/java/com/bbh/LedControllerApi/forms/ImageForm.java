package com.bbh.LedControllerApi.forms;

import org.springframework.stereotype.Component;
@Component
public class ImageForm {
    public boolean reset;
    public String inputFile;
    public Double brightness;
    public Integer[] cropImage;
    public Integer[] scale;
    public boolean disableAutoscale;
    public Integer square;
    public boolean transpose;
    public Double rotate;
    public String saveFile;
    public Integer[] matrix;
}