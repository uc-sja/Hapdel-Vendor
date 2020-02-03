
package com.utility.hapdelvendor.Model.SearchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.utility.hapdelvendor.Model.ProducModel.Product;

import java.util.List;

public class SearchResultModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Product> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

}
