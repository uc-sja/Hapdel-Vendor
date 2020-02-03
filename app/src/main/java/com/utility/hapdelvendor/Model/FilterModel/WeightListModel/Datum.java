
package com.utility.hapdelvendor.Model.FilterModel.WeightListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("per")
    @Expose
    private String per;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getPer() {
        return per;
    }

    public void setPer(String per) {
        this.per = per;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
