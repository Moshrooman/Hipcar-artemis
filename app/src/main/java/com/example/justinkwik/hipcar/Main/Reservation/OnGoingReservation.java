package com.example.justinkwik.hipcar.Main.Reservation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class OnGoingReservation {

    public OnGoingReservation(int id, String full_name, String email, String contact_number, String pickup_address, String pickup_date, String return_date, int total_amount, int payment_status, String actual_pickup_date, String actual_return_date, String i_token, String i_session_key, int actual_total_amount, int owed_amount, int pickup_km, int return_km, int excess_km_charge, int total_free_km, int km_limit, int km_nominal, boolean is_owner, VehicleRates[] vehicle_rates) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.contact_number = contact_number;
        this.pickup_address = pickup_address;
        this.pickup_date = pickup_date;
        this.return_date = return_date;
        this.total_amount = total_amount;
        this.payment_status = payment_status;
        this.actual_pickup_date = actual_pickup_date;
        this.actual_return_date = actual_return_date;
        this.i_token = i_token;
        this.i_session_key = i_session_key;
        this.actual_total_amount = actual_total_amount;
        this.owed_amount = owed_amount;
        this.pickup_km = pickup_km;
        this.return_km = return_km;
        this.excess_km_charge = excess_km_charge;
        this.total_free_km = total_free_km;
        this.km_limit = km_limit;
        this.km_nominal = km_nominal;
        this.is_owner = is_owner;
        this.vehicle_rates = vehicle_rates;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("email")
    private String email;

    @SerializedName("contact_number")
    private String contact_number;

    @SerializedName("pickup_address")
    private String pickup_address;

    @SerializedName("pickup_date")
    private String pickup_date;

    @SerializedName("return_date")
    private String return_date;

    @SerializedName("total_amount")
    private int total_amount;

    @SerializedName("payment_status")
    private int payment_status;

    @SerializedName("actual_pickup_date")
    private String actual_pickup_date;

    @SerializedName("actual_return_date")
    private String actual_return_date;

    @SerializedName("i_token")
    private String i_token;

    @SerializedName("i_session_key")
    private String i_session_key;

    @SerializedName("actual_total_amount")
    private int actual_total_amount;

    @SerializedName("owed_amount")
    private int owed_amount;

    @SerializedName("pickup_km")
    private int pickup_km;

    @SerializedName("return_km")
    private int return_km;

    @SerializedName("excess_km_charge")
    private int excess_km_charge;

    @SerializedName("total_free_km")
    private int total_free_km;

    @SerializedName("km_limit")
    private int km_limit;

    @SerializedName("km_nominal")
    private int km_nominal;

    @SerializedName("is_owner")
    private boolean is_owner;

    @SerializedName("vehicle_rates")
    private VehicleRates[] vehicle_rates;

    public int getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getPickup_address() {
        return pickup_address;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public String getActual_pickup_date() {
        return actual_pickup_date;
    }

    public String getActual_return_date() {
        return actual_return_date;
    }

    public String getI_token() {
        return i_token;
    }

    public String getI_session_key() {
        return i_session_key;
    }

    public int getActual_total_amount() {
        return actual_total_amount;
    }

    public int getOwed_amount() {
        return owed_amount;
    }

    public int getPickup_km() {
        return pickup_km;
    }

    public int getReturn_km() {
        return return_km;
    }

    public int getExcess_km_charge() {
        return excess_km_charge;
    }

    public int getTotal_free_km() {
        return total_free_km;
    }

    public int getKm_limit() {
        return km_limit;
    }

    public int getKm_nominal() {
        return km_nominal;
    }

    public boolean is_owner() {
        return is_owner;
    }

    public VehicleRates[] getVehicle_rates() {
        return vehicle_rates;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

    public void setActual_pickup_date(String actual_pickup_date) {
        this.actual_pickup_date = actual_pickup_date;
    }

    public void setActual_return_date(String actual_return_date) {
        this.actual_return_date = actual_return_date;
    }

    public void setI_token(String i_token) {
        this.i_token = i_token;
    }

    public void setI_session_key(String i_session_key) {
        this.i_session_key = i_session_key;
    }

    public void setActual_total_amount(int actual_total_amount) {
        this.actual_total_amount = actual_total_amount;
    }

    public void setOwed_amount(int owed_amount) {
        this.owed_amount = owed_amount;
    }

    public void setPickup_km(int pickup_km) {
        this.pickup_km = pickup_km;
    }

    public void setReturn_km(int return_km) {
        this.return_km = return_km;
    }

    public void setExcess_km_charge(int excess_km_charge) {
        this.excess_km_charge = excess_km_charge;
    }

    public void setTotal_free_km(int total_free_km) {
        this.total_free_km = total_free_km;
    }

    public void setKm_limit(int km_limit) {
        this.km_limit = km_limit;
    }

    public void setKm_nominal(int km_nominal) {
        this.km_nominal = km_nominal;
    }

    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }

    public void setVehicle_rates(VehicleRates[] vehicle_rates) {
        this.vehicle_rates = vehicle_rates;
    }
}
