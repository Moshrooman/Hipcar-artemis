package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.VehicleStatus;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 29/05/2017.
 */
public class Position {

    public Position(String timestamp, Float lon, Float lat, int quality, int satellites_in_use, String hdop, Float speed_over_ground) {
        this.timestamp = timestamp;
        this.lon = lon;
        this.lat = lat;
        this.quality = quality;
        this.satellites_in_use = satellites_in_use;
        this.hdop = hdop;
        this.speed_over_ground = speed_over_ground;
    }

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("lon")
    private Float lon;

    @SerializedName("lat")
    private Float lat;

    @SerializedName("quality")
    private int quality;

    @SerializedName("satellites_in_use")
    private int satellites_in_use;

    @SerializedName("hdop")
    private String hdop;

    @SerializedName("speed_over_ground")
    private Float speed_over_ground;

    public String getTimestamp() {
        return timestamp;
    }

    public Float getLon() {
        return lon;
    }

    public Float getLat() {
        return lat;
    }

    public int getQuality() {
        return quality;
    }

    public int getSatellites_in_use() {
        return satellites_in_use;
    }

    public String getHdop() {
        return hdop;
    }

    public Float getSpeed_over_ground() {
        return speed_over_ground;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void setSatellites_in_use(int satellites_in_use) {
        this.satellites_in_use = satellites_in_use;
    }

    public void setHdop(String hdop) {
        this.hdop = hdop;
    }

    public void setSpeed_over_ground(Float speed_over_ground) {
        this.speed_over_ground = speed_over_ground;
    }
}
