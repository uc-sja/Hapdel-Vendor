
package com.utility.hapdelvendor.Model.PackageCategoryModel.WeightTypeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("max_weight_kg")
    @Expose
    private String maxWeightKg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaxWeightKg() {
        return maxWeightKg;
    }

    public void setMaxWeightKg(String maxWeightKg) {
        this.maxWeightKg = maxWeightKg;
    }

}
