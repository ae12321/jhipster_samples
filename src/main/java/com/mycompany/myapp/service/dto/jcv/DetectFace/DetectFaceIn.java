package com.mycompany.myapp.service.dto.jcv.DetectFace;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectFaceIn {

    // new しないとだめ
    @JsonProperty("image")
    public Image image = new Image();

    public static class Image {

        @JsonProperty("data")
        public String data;
    }

    @JsonProperty("model")
    public String model = "JCV_FACE_K25000";
}
