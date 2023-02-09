package com.mycompany.myapp.web.rest;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mycompany.myapp.domain.Representative;
import com.mycompany.myapp.repository.RepresentativeRepository;
import com.mycompany.myapp.service.CallJcvApiService;
import com.mycompany.myapp.service.dto.jcv.DetectFace.DetectFaceClient;
import com.mycompany.myapp.service.dto.jcv.DetectFace.DetectFaceIn;
import com.mycompany.myapp.service.dto.jcv.DetectFace.DetectFaceOut;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Transactional
public class SampleResource {

    // private final Logger log = LoggerFactory.getLogger(SeatControlResource.class);

    private static final String ENTITY_NAME = "aseatControl";

    @Autowired
    private CallJcvApiService callJcvApiService;

    // @Value("${jhipster.clientApp.name}")
    // private String applicationName;

    // private final RepresentativeRepository representativeRepository;

    // public SampleResource(RepresentativeRepository representativeRepository) {
    //     this.representativeRepository = representativeRepository;
    // }

    @PostMapping("/sample_01a")
    public ResponseEntity<Void> createRepresentative(@Valid @RequestBody Representative representative) throws URISyntaxException {
        if (representative.getId() != null) {
            throw new BadRequestAlertException("A new representative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // Representative result = representativeRepository.save(representative);
        return ResponseEntity.noContent().build();
    }

    // throwsに指定できるのはURISyntaxException　のみ、
    // ほかは、BeanCreationExceptionやコネクションクローズのエラーになる
    @PostMapping("/face/detect")
    public ResponseEntity<DetectFaceOut> sample01(@RequestBody DetectFaceClient client) throws URISyntaxException {
        DetectFaceIn in = new DetectFaceIn();

        // in check
        try {
            in.image.data = client.image;
            in.model = "JCV_FACE_K25000";
        } catch (Exception e) {
            throw new BadRequestAlertException("client error", "a", "aa");
        }

        // request for jcv anysee
        DetectFaceOut out = null;
        try {
            out = callJcvApiService.detectFace(in);
            System.out.println(out);
            // JSONObject obj = new JSONObject();
            // System.out.println(obj.get("count"));
        } catch (Exception e) {
            throw new BadRequestAlertException("jcv error", "a", "aa");
        }

        return ResponseEntity.ok().body(out);
    }
}
