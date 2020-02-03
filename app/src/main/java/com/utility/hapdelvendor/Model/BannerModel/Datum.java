
package com.utility.hapdelvendor.Model.BannerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("logo_text")
    @Expose
    private String logoText;
    @SerializedName("buton_text")
    @Expose
    private String butonText;
    @SerializedName("offer_text")
    @Expose
    private String offerText;
    @SerializedName("has_subcategories")
    @Expose
    private String hasSubcategories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }

    public String getButonText() {
        return butonText;
    }

    public void setButonText(String butonText) {
        this.butonText = butonText;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String offerText) {
        this.offerText = offerText;
    }

    public String getHasSubcategories() {
        return hasSubcategories;
    }

    public void setHasSubcategories(String hasSubcategories) {
        this.hasSubcategories = hasSubcategories;
    }

}
