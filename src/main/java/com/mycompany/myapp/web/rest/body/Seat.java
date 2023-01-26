package com.mycompany.myapp.web.rest.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("person_name")
    private String personName;

    @JsonProperty("seat_name")
    private String seatName;

    public Seat() {}
}
