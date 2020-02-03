package com.utility.hapdelvendor.Model.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("activated")
    @Expose
    private String activated;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("default_address")
    @Expose
    private List<com.utility.hapdelvendor.Model.AddressModel.Datum> default_address;

    public List<com.utility.hapdelvendor.Model.AddressModel.Datum> getDefault_address() {
        return default_address;
    }

    public void setDefault_address(List<com.utility.hapdelvendor.Model.AddressModel.Datum> default_address) {
        this.default_address = default_address;
    }

    public Datum() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
