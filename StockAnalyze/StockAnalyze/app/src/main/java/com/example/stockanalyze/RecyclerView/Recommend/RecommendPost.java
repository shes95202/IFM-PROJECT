package com.example.stockanalyze.RecyclerView.Recommend;

import java.util.ArrayList;

public class RecommendPost {

    public ArrayList<String> name;
    public ArrayList<String> num;
    public String vf;
    public String unit;
    public String total;

    public RecommendPost(ArrayList<String> Name,ArrayList<String> Num,
                         String Vf,String Unit,String Total){
        this.name = Name;
        this.num = Num;
        this.vf = Vf;
        this.unit = Unit;
        this.total = Total;
    }


    public String getVf() {
        return vf;
    }

    public String getUnit() {
        return unit;
    }

    public String getTotal() {
        return total;
    }
}
