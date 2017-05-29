package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.Price;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class Prices {

    public Prices(int nominal, int total_free_km, int total_amount, PriceList[] price_lists, String start_date, String end_date) {
        this.nominal = nominal;
        this.total_free_km = total_free_km;
        this.total_amount = total_amount;
        this.price_lists = price_lists;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @SerializedName("nominal")
    private int nominal;

    @SerializedName("total_free_km")
    private int total_free_km;

    @SerializedName("total_amount")
    private int total_amount;

    @SerializedName("price_lists")
    private PriceList[] price_lists;

    @SerializedName("start_date")
    private String start_date;

    @SerializedName("end_date")
    private String end_date;

    public int getNominal() {
        return nominal;
    }

    public int getTotal_free_km() {
        return total_free_km;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public PriceList[] getPrice_lists() {
        return price_lists;
    }

    public String getStart_date() {

        if(start_date == null) {

            return "-";

        }

        return start_date;
    }

    public String getEnd_date() {

        if(end_date == null) {

            return "-";

        }

        return end_date;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public void setTotal_free_km(int total_free_km) {
        this.total_free_km = total_free_km;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public void setPrice_lists(PriceList[] price_lists) {
        this.price_lists = price_lists;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
