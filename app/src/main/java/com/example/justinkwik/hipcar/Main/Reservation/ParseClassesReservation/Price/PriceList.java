package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.Price;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class PriceList {

    public PriceList(int count, Rates rates, int total) {
        this.count = count;
        this.rates = rates;
        this.total = total;
    }

    @SerializedName("count")
    private int count;

    @SerializedName("rates")
    private Rates rates;

    @SerializedName("total")
    private int total;

    public int getCount() {
        return count;
    }

    public Rates getRates() {
        return rates;
    }

    public int getTotal() {
        return total;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
