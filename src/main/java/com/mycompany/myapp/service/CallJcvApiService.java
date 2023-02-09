package com.mycompany.myapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mycompany.myapp.service.dto.jcv.DetectFace.DetectFaceIn;
import com.mycompany.myapp.service.dto.jcv.DetectFace.DetectFaceOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CallJcvApiService {

    private String API_KEY = "0e4f14a9-d37b-4d1a-bb9c-7ff7b3b07265";
    private String BASE_URL = "https://anysee.ap-northeast-1.cloud.japancv.co.jp";

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    public CallJcvApiService() {}

    public DetectFaceOut detectFace(DetectFaceIn in) throws JsonProcessingException {
        // public String detectFace(DetectFaceIn in) throws JsonProcessingException {
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=utf-8");
        headers.add("api-key", API_KEY);
        headers.add("accept", "application/json");

        var url = "https://anysee.ap-northeast-1.cloud.japancv.co.jp/api/v1/entities/faces/detect";

        HttpEntity<DetectFaceIn> entity = new HttpEntity<>(in, headers);
        RestTemplate restTemplate = new RestTemplate();
        //
        ResponseEntity<DetectFaceOut> response = restTemplate.exchange(url, HttpMethod.POST, entity, DetectFaceOut.class);
        // ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
        // return restTemplate.postForObject(requestBody, DetectFaceOut.class);
    }
}
