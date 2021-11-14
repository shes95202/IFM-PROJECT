package com.example.stockanalyze.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET(".")
    Call<JsonObject> getAllGroup();

    @GET("stock_select_api.php/")
    Call<JsonObject> searchAllStock(
            @Query("select_text") String searchInfo
    );

    @GET("stock_select_individual_api.php/")
    Call<JsonObject> getStockDetail(
            @Query("select_text") String searchInfo
    );

}
