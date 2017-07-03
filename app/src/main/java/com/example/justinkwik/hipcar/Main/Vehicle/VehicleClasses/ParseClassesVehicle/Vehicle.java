package com.example.justinkwik.hipcar.Main.Vehicle.VehicleClasses.ParseClassesVehicle;

import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Station.PickupStation;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Vehicle.VehicleModel;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Vehicle.VehicleRates;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 03/07/2017.
 */
public class Vehicle {

    public Vehicle(int id, String plate_number, String color, int year, int capacity, boolean is_active, String transmission, String qnr, int excess_km_charge, String registration_expire, String created, String updated, String deleted, int partner_id, int station_id, int model_id, VehicleModel vehicle_model, VehicleRates[] vehicle_rates, PickupStation station) {
        this.id = id;
        this.plate_number = plate_number;
        this.color = color;
        this.year = year;
        this.capacity = capacity;
        this.is_active = is_active;
        this.transmission = transmission;
        this.qnr = qnr;
        this.excess_km_charge = excess_km_charge;
        this.registration_expire = registration_expire;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
        this.partner_id = partner_id;
        this.station_id = station_id;
        this.model_id = model_id;
        this.vehicle_model = vehicle_model;
        this.vehicle_rates = vehicle_rates;
        this.station = station;
    }

    private boolean expanded = false;

    @SerializedName("id")
    private int id;

    @SerializedName("plate_number")
    private String plate_number;

    @SerializedName("color")
    private String color;

    @SerializedName("year")
    private int year;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("is_active")
    private boolean is_active;

    @SerializedName("transmission")
    private String transmission;

    @SerializedName("qnr")
    private String qnr;

    @SerializedName("excess_km_charge")
    private int excess_km_charge;

    @SerializedName("registration_expire")
    private String registration_expire;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("deleted")
    private String deleted;

    @SerializedName("partner_id")
    private int partner_id;

    @SerializedName("station_id")
    private int station_id;

    @SerializedName("model_id")
    private int model_id;

    @SerializedName("vehicle_rates")
    private VehicleRates[] vehicle_rates;

    @SerializedName("station")
    private PickupStation station;

    @SerializedName("vehicle_model")
    private VehicleModel vehicle_model;

    public int getId() {
        return id;
    }

    public String getPlate_number() {

        if(plate_number == null) {

            return "-";

        }

        return plate_number;
    }

    public String getColor() {

        if(color == null) {

            return "-";

        }

        return color;
    }

    public int getYear() {

        return year;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean is_active() {
        return is_active;
    }

    public String getTransmission() {

        if(transmission == null) {

            return "-";

        } else if (transmission.equals("0")) {

            return "Automatic";

        } else {

            return "Manual";

        }

    }

    public String getQnr() {

        if(qnr == null) {

            return "-";

        }

        return qnr;
    }

    public int getExcess_km_charge() {
        return excess_km_charge;
    }

    public String getRegistration_expire() {

        if(registration_expire == null) {

            return "-";

        }

        return registration_expire;
    }

    public String getCreated() {

        if(created == null) {

            return "-";

        }

        return created;
    }

    public String getUpdated() {

        if(updated == null) {

            return "-";

        }

        return updated;
    }

    public String getDeleted() {

        if(deleted == null) {

            return "-";

        }

        return deleted;
    }

    public VehicleRates[] getVehicle_rates() {
        return vehicle_rates;
    }

    public PickupStation getStation() {

        if(station == null) {
            return new PickupStation(0, "-", "-", "-", "-", false, "-", "-", "-");
        }

        return station;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public int getStation_id() {
        return station_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public VehicleModel getVehicle_model() {
        return vehicle_model;
    }

    public boolean getExpanded() {

        return expanded;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setQnr(String qnr) {
        this.qnr = qnr;
    }

    public void setExcess_km_charge(int excess_km_charge) {
        this.excess_km_charge = excess_km_charge;
    }

    public void setRegistration_expire(String registration_expire) {
        this.registration_expire = registration_expire;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public void setVehicle_model(VehicleModel vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public void setVehicle_rates(VehicleRates[] vehicle_rates) {
        this.vehicle_rates = vehicle_rates;
    }

    public void setStation(PickupStation station) {
        this.station = station;
    }

    public void setExpanded(boolean expanded) {

        this.expanded = expanded;

    }
}
