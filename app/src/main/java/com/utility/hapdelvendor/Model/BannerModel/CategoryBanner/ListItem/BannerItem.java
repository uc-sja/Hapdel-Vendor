package com.utility.hapdelvendor.Model.BannerModel.CategoryBanner.ListItem;


import com.utility.hapdelvendor.Model.BannerModel.CategoryBanner.Banner;

public class BannerItem extends ListItem {
    private Banner banner;

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_CATEGORY;
    }
}
