
package com.utility.hapdelvendor.Model.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("profile")
    @Expose
    private Object profile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("otp")
    @Expose
    private Object otp;
    @SerializedName("forgot_token")
    @Expose
    private Object forgotToken;
    @SerializedName("request_date")
    @Expose
    private Object requestDate;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("activated")
    @Expose
    private String activated;
    @SerializedName("access_role")
    @Expose
    private Object accessRole;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("registration_date")
    @Expose
    private String registrationDate;
    @SerializedName("access_token")
    @Expose
    private String accessToken;

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

    public Object getProfile() {
        return profile;
    }

    public void setProfile(Object profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
        this.otp = otp;
    }

    public Object getForgotToken() {
        return forgotToken;
    }

    public void setForgotToken(Object forgotToken) {
        this.forgotToken = forgotToken;
    }

    public Object getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Object requestDate) {
        this.requestDate = requestDate;
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

    public Object getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(Object accessRole) {
        this.accessRole = accessRole;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
