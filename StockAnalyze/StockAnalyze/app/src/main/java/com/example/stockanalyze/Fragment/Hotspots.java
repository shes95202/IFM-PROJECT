package com.example.stockanalyze.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.Http.HttpBase;
import com.example.stockanalyze.NavigationHost;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.Hotspots.HotspotsAdapter;
import com.example.stockanalyze.RecyclerView.Hotspots.HotspotsPost;
import com.example.stockanalyze.Retrofit.RetrofitService;
import com.example.stockanalyze.SystemStyle;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Hotspots extends Fragment {

    SystemStyle systemStyle = new SystemStyle();
    HttpBase httpBase = new HttpBase();

    RetrofitService retrofitService;

    RecyclerView recyclerView;
    HotspotsAdapter adapter;
    ConstraintLayout progressView;
    ArrayList<HotspotsPost> data;

    /**
     * 初始化元件
     * */
    private void initLayout(View view){
        recyclerView = view.findViewById(R.id.hots_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        progressView = view.findViewById(R.id.hots_view);

        data = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_hotspots,container,false);

        systemStyle.getScreenSize(requireActivity());
        systemStyle.setTitleBarColor(requireActivity());
        initLayout(view);
        getHotspots();

        return view;
    }

    /**
     * call api 取得資料
     * */
    private void getHotspots(){
        progressView.setVisibility(View.VISIBLE);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpBase.getHotspotsApi)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        Call<JsonObject> call = retrofitService.getAllGroup();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressView.setVisibility(View.INVISIBLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Parse(response.body().toString());
                    }
                }else{
                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 處理api回傳的資料
     * */
    private void Parse(String jsonObject){
        try {
            JSONObject object = new JSONObject(jsonObject);
            int count = object.getJSONArray("data").length();

            for (int index = 0; index < count; index++) {
                
                String name = object.getJSONArray("data").getJSONObject(index).getString("company_name");
                
                if(!name.equals("null")){
                    String num = object.getJSONArray("data").getJSONObject(index).getString("stock_symbol");
                    String shortLine = object.getJSONArray("data").getJSONObject(index).getString("short_profit");
                    String longLine = object.getJSONArray("data").getJSONObject(index).getString("long_profit");

                    HotspotsPost post = new HotspotsPost(name,num,shortLine,longLine);
                    data.add(post);
                }else{
                    Toast.makeText(requireActivity(), "查無資料", Toast.LENGTH_SHORT).show();
                }
                
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 激活recyclerView
        adapter = new HotspotsAdapter(data,requireActivity());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            HotspotsPost post = data.get(position);

            Bundle bundle = new Bundle();
            bundle.putString("name",post.name);
            bundle.putString("num",post.num);
            bundle.putString("backView","Hotspots");
            requireActivity().getSupportFragmentManager().setFragmentResult("detail",bundle);

            ((NavigationHost)requireActivity()).NavigateTo(new Detail(),"Detail");
        });
    }
}
