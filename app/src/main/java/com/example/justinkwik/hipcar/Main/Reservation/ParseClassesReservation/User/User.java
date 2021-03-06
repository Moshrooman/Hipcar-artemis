package com.example.justinkwik.hipcar.Main.Reservation.ParseClassesReservation.User;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 23/05/2017.
 */
public class User {

    public User(int id, String email, String first_name, String last_name, String contact_number, int balance, String installation_id, String activated_date, String suspended_date, String rejected_date, String reset_token, String birth_date, String gender, String created, String updated, String deleted) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_number = contact_number;
        this.balance = balance;
        this.installation_id = installation_id;
        this.activated_date = activated_date;
        this.suspended_date = suspended_date;
        this.rejected_date = rejected_date;
        this.reset_token = reset_token;
        this.birth_date = birth_date;
        this.gender = gender;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("contact_number")
    private String contact_number;

    @SerializedName("balance")
    private int balance;

    @SerializedName("installation_id")
    private String installation_id;

    @SerializedName("activated_date")
    private String activated_date;

    @SerializedName("suspended_date")
    private String suspended_date;

    @SerializedName("rejected_date")
    private String rejected_date;

    @SerializedName("reset_token")
    private String reset_token;

    @SerializedName("birth_date")
    private String birth_date;

    @SerializedName("gender")
    private String gender;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("deleted")
    private String deleted;

    public int getId() {
        return id;
    }

    public String getEmail() {

        if(email == null) {

            return "-";

        }

        return email;
    }

    public String getFirst_name() {

        if(first_name == null) {

            return "-";

        }

        return first_name;
    }

    public String getLast_name() {

        if(last_name == null) {

            return "-";

        }

        return last_name;
    }

    public String getContact_number() {

        if(contact_number == null) {

            return "-";

        }

        return contact_number;
    }

    public int getBalance() {
        return balance;
    }

    public String getInstallation_id() {

        if(installation_id == null) {

            return "-";

        }

        return installation_id;
    }

    public String getActivated_date() {

        if(activated_date == null) {

            return "-";

        }

        return activated_date;
    }

    public String getSuspended_date() {

        if(suspended_date == null) {

            return "-";

        }

        return suspended_date;
    }

    public String getRejected_date() {

        if(rejected_date == null) {

            return "-";

        }

        return rejected_date;
    }

    public String getReset_token() {

        if(reset_token == null) {

            return "-";

        }

        return reset_token;
    }

    public String getBirth_date() {

        if(birth_date == null) {

            return "-";

        }

        return birth_date;
    }

    public String getGender() {

        if(gender == null) {

            return "-";

        }

        return gender;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setInstallation_id(String installation_id) {
        this.installation_id = installation_id;
    }

    public void setActivated_date(String activated_date) {
        this.activated_date = activated_date;
    }

    public void setSuspended_date(String suspended_date) {
        this.suspended_date = suspended_date;
    }

    public void setRejected_date(String rejected_date) {
        this.rejected_date = rejected_date;
    }

    public void setReset_token(String reset_token) {
        this.reset_token = reset_token;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
