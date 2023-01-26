package com.mycompany.myapp.web.rest.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class ConfirmBody implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("main_user_name")
    private String mainUserName;

    @JsonProperty("seats")
    private List<Seat> seats;

    public String getMainUserName() {
        return mainUserName;
    }

    public void setMainUserName(String mainUserName) {
        this.mainUserName = mainUserName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
