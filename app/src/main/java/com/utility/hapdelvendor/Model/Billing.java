package com.utility.hapdelvendor.Model;

public class Billing {

    String item_subtoal;
    String item_discount;
    String coupon_dicount;
    String shipping_charges;
    String grand_total;
    String service_time;

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getItem_subtoal() {
        return item_subtoal;
    }

    public void setItem_subtoal(String item_subtoal) {
        this.item_subtoal = item_subtoal;
    }

    public String getItem_discount() {
        return item_discount;
    }

    public void setItem_discount(String item_discount) {
        this.item_discount = item_discount;
    }

    public String getCoupon_dicount() {
        return coupon_dicount;
    }

    public void setCoupon_dicount(String coupon_dicount) {
        this.coupon_dicount = coupon_dicount;
    }

    public String getShipping_charges() {
        return shipping_charges;
    }

    public void setShipping_charges(String shipping_charges) {
        this.shipping_charges = shipping_charges;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }
}
