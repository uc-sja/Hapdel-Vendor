
package com.utility.hapdelvendor.Model.UploadDocModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("document_1")
    @Expose
    private String document1;
    @SerializedName("document_2")
    @Expose
    private String document2;
    @SerializedName("document_3")
    @Expose
    private String document3;
    @SerializedName("document_4")
    @Expose
    private String document4;
    @SerializedName("store_logo")
    @Expose
    private String storeLogo;
    @SerializedName("document_5")
    @Expose
    private String document5;

    public String getDocument1() {
        return document1;
    }

    public void setDocument1(String document1) {
        this.document1 = document1;
    }

    public String getDocument2() {
        return document2;
    }

    public void setDocument2(String document2) {
        this.document2 = document2;
    }

    public String getDocument3() {
        return document3;
    }

    public void setDocument3(String document3) {
        this.document3 = document3;
    }

    public String getDocument4() {
        return document4;
    }

    public void setDocument4(String document4) {
        this.document4 = document4;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getDocument5() {
        return document5;
    }

    public void setDocument5(String document5) {
        this.document5 = document5;
    }

}
