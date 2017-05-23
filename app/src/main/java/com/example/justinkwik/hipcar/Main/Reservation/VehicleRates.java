package com.example.justinkwik.hipcar.Main.Reservation;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class VehicleRates {

    public VehicleRates(int id, JSONObject vehicle, int price, int km_limit, int day, int nominal) {
        this.id = id;
        this.price = price;
        this.km_limit = km_limit;
        this.day = day;
        this.nominal = nominal;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("price")
    private int price;

    @SerializedName("km_limit")
    private int km_limit;

    @SerializedName("day")
    private int day;

    @SerializedName("nominal")
    private int nominal;

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getKm_limit() {
        return km_limit;
    }

    public int getDay() {
        return day;
    }

    public int getNominal() {
        return nominal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setKm_limit(int km_limit) {
        this.km_limit = km_limit;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
