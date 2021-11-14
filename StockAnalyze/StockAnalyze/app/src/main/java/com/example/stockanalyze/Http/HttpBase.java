package com.example.stockanalyze.Http;

public class HttpBase {

        public String urlBase = "http://hourent.asuscomm.com/stock/";
//    public String urlBase = "http://9.9.9.6/stock/";

    public String getRecommendApi = urlBase + "stock_select_recommend_api.php/";

    public String getHotspotsApi = urlBase + "stock_select_popular_api.php/";

    public String getSearchStock = urlBase + "stock_select_all_api.php/";


}
