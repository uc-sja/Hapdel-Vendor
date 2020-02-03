
package com.utility.hapdelvendor.Model.BannerModel.CategoryBanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("banner_img")
    @Expose
    private String bannerImg;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product_category_id")
    @Expose
    private String productCategoryId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

}
