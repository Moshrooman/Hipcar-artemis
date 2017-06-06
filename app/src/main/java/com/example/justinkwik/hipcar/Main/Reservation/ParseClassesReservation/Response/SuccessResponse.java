package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 04/06/2017.
 */
public class SuccessResponse {

    public SuccessResponse(String message) {
        this.message = message;
    }

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
