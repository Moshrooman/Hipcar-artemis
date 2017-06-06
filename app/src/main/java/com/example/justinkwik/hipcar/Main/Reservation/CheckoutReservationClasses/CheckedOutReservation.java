package com.example.justinkwik.hipcar.Main.Reservation.CheckoutReservationClasses;

import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Station.PickupStation;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Station.ReturnStation;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.User.User;
import com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Vehicle.Vehicle;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 06/06/2017.
 */
public class CheckedOutReservation {

    public CheckedOutReservation(int id, String full_name, String email, String contact_number, String pickup_address, String pickup_date, String return_date, int total_amount, int payment_type, int payment_status, String actual_pickup_date, String actual_return_date, String i_token, String i_session_key, int owed_amount, Float pickup_km, Float return_km, int excess_km_charge, Float total_free_km, Float km_limit, Float km_nominal, boolean is_owner, String vehicle_rates, String created, String updated, String deleted, int user_id, int vehicle_id, int pickup_station_id, int return_station_id, User user, ReturnStation return_station, PickupStation pickup_station, Vehicle vehicle) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.contact_number = contact_number;
        this.pickup_address = pickup_address;
        this.pickup_date = pickup_date;
        this.return_date = return_date;
        this.total_amount = total_amount;
        this.payment_type = payment_type;
        this.payment_status = payment_status;
        this.actual_pickup_date = actual_pickup_date;
        this.actual_return_date = actual_return_date;
        this.i_token = i_token;
        this.i_session_key = i_session_key;
        this.owed_amount = owed_amount;
        this.pickup_km = pickup_km;
        this.return_km = return_km;
        this.excess_km_charge = excess_km_charge;
        this.total_free_km = total_free_km;
        this.km_limit = km_limit;
        this.km_nominal = km_nominal;
        this.is_owner = is_owner;
        this.vehicle_rates = vehicle_rates;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
        this.user_id = user_id;
        this.vehicle_id = vehicle_id;
        this.pickup_station_id = pickup_station_id;
        this.return_station_id = return_station_id;
        this.user = user;
        this.return_station = return_station;
        this.pickup_station = pickup_station;
        this.vehicle = vehicle;
    }

    private boolean expanded = false;

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

    @SerializedName("payment_type")
    private int payment_type;

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

    @SerializedName("owed_amount")
    private int owed_amount;

    @SerializedName("pickup_km")
    private Float pickup_km;

    @SerializedName("return_km")
    private Float return_km;

    @SerializedName("excess_km_charge")
    private int excess_km_charge;

    @SerializedName("total_free_km")
    private Float total_free_km;

    @SerializedName("km_limit")
    private Float km_limit;

    @SerializedName("km_nominal")
    private Float km_nominal;

    @SerializedName("is_owner")
    private boolean is_owner;

    @SerializedName("vehicle_rates")
    private String vehicle_rates;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("deleted")
    private String deleted;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("vehicle_id")
    private int vehicle_id;

    @SerializedName("pickup_station_id")
    private int pickup_station_id;

    @SerializedName("return_station_id")
    private int return_station_id;

    @SerializedName("user")
    private User user;

    @SerializedName("return_station")
    private ReturnStation return_station;

    @SerializedName("pickup_station")
    private PickupStation pickup_station;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    public boolean getExpanded() {

        return expanded;

    }

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

    public int getPayment_type() {
        return payment_type;
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

    public int getOwed_amount() {
        return owed_amount;
    }

    public Float getPickup_km() {
        return pickup_km;
    }

    public Float getReturn_km() {
        return return_km;
    }

    public int getExcess_km_charge() {
        return excess_km_charge;
    }

    public Float getTotal_free_km() {
        return total_free_km;
    }

    public Float getKm_limit() {
        return km_limit;
    }

    public Float getKm_nominal() {
        return km_nominal;
    }

    public boolean is_owner() {
        return is_owner;
    }

    public String getVehicle_rates() {
        return vehicle_rates;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public String getDeleted() {
        return deleted;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public int getPickup_station_id() {
        return pickup_station_id;
    }

    public int getReturn_station_id() {
        return return_station_id;
    }

    public User getUser() {
        return user;
    }

    public ReturnStation getReturn_station() {
        return return_station;
    }

    public PickupStation getPickup_station() {
        return pickup_station;
    }

    public Vehicle getVehicle() {
        return vehicle;
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

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
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

    public void setOwed_amount(int owed_amount) {
        this.owed_amount = owed_amount;
    }

    public void setPickup_km(Float pickup_km) {
        this.pickup_km = pickup_km;
    }

    public void setReturn_km(Float return_km) {
        this.return_km = return_km;
    }

    public void setExcess_km_charge(int excess_km_charge) {
        this.excess_km_charge = excess_km_charge;
    }

    public void setTotal_free_km(Float total_free_km) {
        this.total_free_km = total_free_km;
    }

    public void setKm_limit(Float km_limit) {
        this.km_limit = km_limit;
    }

    public void setKm_nominal(Float km_nominal) {
        this.km_nominal = km_nominal;
    }

    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }

    public void setVehicle_rates(String vehicle_rates) {
        this.vehicle_rates = vehicle_rates;
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

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public void setPickup_station_id(int pickup_station_id) {
        this.pickup_station_id = pickup_station_id;
    }

    public void setReturn_station_id(int return_station_id) {
        this.return_station_id = return_station_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReturn_station(ReturnStation return_station) {
        this.return_station = return_station;
    }

    public void setPickup_station(PickupStation pickup_station) {
        this.pickup_station = pickup_station;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setExpanded(boolean expanded) {

        this.expanded = expanded;

    }

}
