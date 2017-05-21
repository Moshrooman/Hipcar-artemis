package com.example.justinkwik.hipcar.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Justin Kwik on 21/05/2017.
 */
public class UserCredentials {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("is_active")
    private boolean is_active;

    @SerializedName("created")
    private String created;

    @SerializedName("updated")
    private String updated;

    @SerializedName("deleted")
    private boolean deleted;

    @SerializedName("roles")
    private String[] roles;

    @SerializedName("token")
    private String token;

    public UserCredentials(int id, String email, String name, boolean is_active, String created, String updated, boolean deleted, String[] roles, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.is_active = is_active;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
        this.roles = roles;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean is_active() {
        return is_active;
    }

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String[] getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
