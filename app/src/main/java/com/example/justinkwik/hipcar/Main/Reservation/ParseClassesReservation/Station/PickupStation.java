package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Station;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class PickupStation {

    public PickupStation(int id, String name, String address, String latitude, String longitude, boolean is_active, String created, String updated, String deleted) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_active = is_active;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("is_active")
    private boolean is_active;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("deleted")
    private String deleted;

    public int getId() {
        return id;
    }

    public String getName() {

        if(name == null) {

            return "-";

        }

        return name;
    }

    public String getAddress() {

        if(address == null) {

            return "-";

        }

        return address;
    }

    public String getLatitude() {

        if(latitude == null) {

            return "-";

        }

        return latitude;
    }

    public String getLongitude() {

        if(longitude == null) {

            return "-";

        }

        return longitude;
    }

    public boolean is_active() {
        return is_active;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
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

}
