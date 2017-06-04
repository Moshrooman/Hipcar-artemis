package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 29/05/2017.
 */
public class VehicleStatus {

    public VehicleStatus(Position position, String immobilizer, String ignition, String central_lock, String bluetooth_connection, Float board_voltage, com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.rfid_tag_states rfid_tag_states, String central_lock_last_command, String alarm_input, boolean lowFuelLevelAlarm, Float mileage_since_immobilizer_unlock, Float mileage) {
        this.position = position;
        this.immobilizer = immobilizer;
        this.ignition = ignition;
        this.central_lock = central_lock;
        this.bluetooth_connection = bluetooth_connection;
        this.board_voltage = board_voltage;
        this.rfid_tag_states = rfid_tag_states;
        this.central_lock_last_command = central_lock_last_command;
        this.alarm_input = alarm_input;
        LowFuelLevelAlarm = lowFuelLevelAlarm;
        this.mileage_since_immobilizer_unlock = mileage_since_immobilizer_unlock;
        this.mileage = mileage;
    }

    @SerializedName("position")
    private Position position;

    @SerializedName("immobilizer")
    private String immobilizer;

    @SerializedName("ignition")
    private String ignition;

    @SerializedName("central_lock")
    private String central_lock;

    @SerializedName("bluetooth_connection")
    private String bluetooth_connection;

    @SerializedName("board_voltage")
    private Float board_voltage;

    @SerializedName("rfid_tag_states")
    private rfid_tag_states rfid_tag_states;

    @SerializedName("central_lock_last_command")
    private String central_lock_last_command;

    @SerializedName("alarm_input")
    private String alarm_input;

    @SerializedName("LowFuelLevelAlarm")
    private boolean LowFuelLevelAlarm;

    @SerializedName("mileage_since_immobilizer_unlock")
    private Float mileage_since_immobilizer_unlock;

    @SerializedName("mileage")
    private Float mileage;

    public Position getPosition() {
        return position;
    }

    public String getImmobilizer() {
        return immobilizer;
    }

    public String getIgnition() {
        return ignition;
    }

    public String getCentral_lock() {
        return central_lock;
    }

    public String getBluetooth_connection() {
        return bluetooth_connection;
    }

    public Float getBoard_voltage() {
        return board_voltage;
    }

    public com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.rfid_tag_states getRfid_tag_states() {
        return rfid_tag_states;
    }

    public String getCentral_lock_last_command() {
        return central_lock_last_command;
    }

    public String getAlarm_input() {
        return alarm_input;
    }

    public boolean isLowFuelLevelAlarm() {
        return LowFuelLevelAlarm;
    }

    public Float getMileage_since_immobilizer_unlock() {
        return mileage_since_immobilizer_unlock;
    }

    public Float getMileage() {
        return mileage;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setImmobilizer(String immobilizer) {
        this.immobilizer = immobilizer;
    }

    public void setIgnition(String ignition) {
        this.ignition = ignition;
    }

    public void setCentral_lock(String central_lock) {
        this.central_lock = central_lock;
    }

    public void setBluetooth_connection(String bluetooth_connection) {
        this.bluetooth_connection = bluetooth_connection;
    }

    public void setBoard_voltage(Float board_voltage) {
        this.board_voltage = board_voltage;
    }

    public void setRfid_tag_states(com.example.justinkwik.hipcar.Main.Reservation.ParseClassesOnGoing.VehicleStatus.rfid_tag_states rfid_tag_states) {
        this.rfid_tag_states = rfid_tag_states;
    }

    public void setCentral_lock_last_command(String central_lock_last_command) {
        this.central_lock_last_command = central_lock_last_command;
    }

    public void setAlarm_input(String alarm_input) {
        this.alarm_input = alarm_input;
    }

    public void setLowFuelLevelAlarm(boolean lowFuelLevelAlarm) {
        LowFuelLevelAlarm = lowFuelLevelAlarm;
    }

    public void setMileage_since_immobilizer_unlock(Float mileage_since_immobilizer_unlock) {
        this.mileage_since_immobilizer_unlock = mileage_since_immobilizer_unlock;
    }

    public void setMileage(Float mileage) {
        this.mileage = mileage;
    }
}
