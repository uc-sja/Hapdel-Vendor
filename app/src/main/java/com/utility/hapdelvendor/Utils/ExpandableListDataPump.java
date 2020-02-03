package com.utility.hapdelvendor.Utils;


import com.utility.hapdelvendor.Model.FilterModel.WeightListModel.Datum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    private static HashMap<String, List<String>> expandableListDetail;

    public static void setData(List<Datum> weightDatum) {
        expandableListDetail = new HashMap<String, List<String>>();

        List<String> brands = new ArrayList<String>();
        brands.add("Local");
        brands.add("Premium");

        List<String> weightList = new ArrayList<String>();
        for(Datum datum: weightDatum){
            weightList.add(datum.getPer()+" "+datum.getUnit());
        }

        expandableListDetail.put("Brands", brands);
        expandableListDetail.put("Weight", weightList);
    }

    public static HashMap<String, List<String>> getData() {
        return expandableListDetail;
    }
}