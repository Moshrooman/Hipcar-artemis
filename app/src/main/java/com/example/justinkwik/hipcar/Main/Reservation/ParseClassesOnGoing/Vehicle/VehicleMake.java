package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.Vehicle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class VehicleMake {

    public VehicleMake(int id, String name, boolean is_active) {
        this.id = id;
        this.name = name;
        this.is_active = is_active;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("is_active")
    private boolean is_active;

    public int getId() {
        return id;
    }

    public String getName() {

        if(name == null) {

            return "-";

        }

        return name;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
