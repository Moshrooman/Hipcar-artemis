package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Vehicle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class VehicleRatesId {

    public VehicleRatesId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
