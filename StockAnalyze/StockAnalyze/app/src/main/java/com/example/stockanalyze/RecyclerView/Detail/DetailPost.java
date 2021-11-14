package com.example.stockanalyze.RecyclerView.Detail;

import java.util.ArrayList;

public class DetailPost {

    //短線
    public ArrayList<String> shortUps;
    public ArrayList<String> shortDowns;
    public ArrayList<String> shortNones;
    //長線
    public ArrayList<String> longUps;
    public ArrayList<String> longDowns;
    public ArrayList<String> longNones;

    public DetailPost(ArrayList<String> sUp, ArrayList<String> sDown, ArrayList<String> sNone,
                      ArrayList<String> lUp, ArrayList<String> lDown, ArrayList<String> lNone) {

        this.shortUps = sUp;
        this.shortDowns = sDown;
        this.shortNones = sNone;
        this.longUps = lUp;
        this.longDowns = lDown;
        this.longNones = lNone;
    }
}
