
package com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum implements Comparable<Datum>{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("has_subcategories")
    @Expose
    private String hasSubcategories;
    @SerializedName("all_has_subcategories")
    @Expose
    private String allHasSubcategories;
    @SerializedName("subcategories")
    @Expose
    private List<Subcategory> subcategories = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getHasSubcategories() {
        return hasSubcategories;
    }

    public void setHasSubcategories(String hasSubcategories) {
        this.hasSubcategories = hasSubcategories;
    }

    public String getAllHasSubcategories() {
        return allHasSubcategories;
    }

    public void setAllHasSubcategories(String allHasSubcategories) {
        this.allHasSubcategories = allHasSubcategories;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    @Override
    public int compareTo(Datum o) {

        if(Double.parseDouble(sortOrder)>Double.parseDouble(o.sortOrder)){
            return 1;
        } else if(Double.valueOf(sortOrder) < Double.valueOf(o.sortOrder)){
            return -1;
        } else {
            return 0;
        }
    }
}
