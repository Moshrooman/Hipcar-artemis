package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.Vehicle;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class VehicleModel {

    public VehicleModel(int id, String name, boolean is_active, int class_int, String image_url, VehicleMake vehicle_make) {
        this.id = id;
        this.name = name;
        this.is_active = is_active;
        this.class_int = class_int;
        this.image_url = image_url;
        this.vehicle_make = vehicle_make;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("is_active")
    private boolean is_active;

    @SerializedName("class")
    private int class_int;

    @SerializedName("image_url")
    private String image_url;

    @SerializedName("vehicle_make")
    private VehicleMake vehicle_make;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean is_active() {
        return is_active;
    }

    public int getClass_int() {
        return class_int;
    }

    public String getImage_url() {
        return image_url;
    }

    public VehicleMake getVehicle_make() {
        return vehicle_make;
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

    public void setClass_int(int class_int) {
        this.class_int = class_int;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setVehicle_make(VehicleMake vehicle_make) {
        this.vehicle_make = vehicle_make;
    }
}
