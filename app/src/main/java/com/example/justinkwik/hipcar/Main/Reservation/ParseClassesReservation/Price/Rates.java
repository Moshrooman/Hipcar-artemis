package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Price;

import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Vehicle.VehicleRatesId;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class Rates {

    public Rates(int id, VehicleRatesId vehicleRatesId, int price, int km_limit, int day, int nominal) {
        this.id = id;
        this.vehicleRatesId = vehicleRatesId;
        this.price = price;
        this.km_limit = km_limit;
        this.day = day;
        this.nominal = nominal;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("vehicleRatesId")
    private VehicleRatesId vehicleRatesId;

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

    public VehicleRatesId getVehicleRatesId() {
        return vehicleRatesId;
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

    public void setVehicleRatesId(VehicleRatesId vehicleRatesId) {
        this.vehicleRatesId = vehicleRatesId;
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
