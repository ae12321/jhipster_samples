package com.mycompany.myapp.service.dto.jcv.DetectFace;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectFaceOut {

    @JsonProperty("model")
    public String model;

    @JsonProperty("model")
    public int count;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
