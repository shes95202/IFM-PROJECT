package com.example.stockanalyze.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.Http.HttpBase;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupPost;
import com.example.stockanalyze.RecyclerView.Recommend.RecommendAdapter;
import com.example.stockanalyze.RecyclerView.Recommend.RecommendPost;
import com.example.stockanalyze.Retrofit.RetrofitService;
import com.example.stockanalyze.SystemStyle;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Recommend extends Fragment {

    SystemStyle systemStyle = new SystemStyle();
    HttpBase httpBase = new HttpBase();

    RetrofitService retrofitService;

    RecyclerView recyclerView;
    ConstraintLayout progressView;
    RecommendAdapter adapter;
    ArrayList<RecommendPost> data;
    Button saveBtn;

    public static ArrayList<MyGroupPost> groupData;

    /**
     * 初始化元件
     * */
    private void initLayout(View view) {
        recyclerView = view.findViewById(R.id.recommend_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        progressView = view.findViewById(R.id.recommend_view);
        saveBtn = view.findViewById(R.id.recommend_bnt);

        data = new ArrayList<>();
        groupData = new ArrayList<>();

        saveBtn.setOnClickListener(v -> {
            getViewData();
        });
    }

    /**
     * 取得組合推薦的列表資料：股票名稱、股票代號、百分比
     * */
    private void getViewData() {
        if(data.size()!=0){
            ArrayList<Integer> dataIndex = adapter.getNowViewData();
            groupData = new ArrayList<>();

            for (int index = 0; index < dataIndex.size(); index++) {
                double percent = div(Double.parseDouble(data.get(index).unit) * 100, Double.parseDouble(data.get(index).total) * 100, 3);
                int arrayIndex = dataIndex.get(index);

                // 判斷取得最後一筆資料時，將最後一筆資料的百分比修改成符合100%
                if (index == dataIndex.size() - 1) {
                    double allOfBeforeValue = 0;
                    for (int i = 0; i < dataIndex.size() - 1; i++) {
                        allOfBeforeValue = allOfBeforeValue + div(Double.parseDouble(data.get(i).unit) * 100, Double.parseDouble(data.get(i).total) * 100, 3);
                    }
                    float endValue = (float) (100 - allOfBeforeValue * 100);
                    groupData.add(new MyGroupPost(
                            data.get(index).name.get(arrayIndex),
                            data.get(index).num.get(arrayIndex),
                            "" + endValue
                    ));
                } else {
                    groupData.add(new MyGroupPost(
                            data.get(index).name.get(arrayIndex),
                            data.get(index).num.get(arrayIndex),
                            "" + ((float) percent) * 100
                    ));
                }

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_recommend, container, false);

        systemStyle.getScreenSize(requireActivity());
        systemStyle.setTitleBarColor(requireActivity());
        initLayout(view);
        getAllGroup();

        return view;
    }

    /**
     * call api 取得資料
     * */
    private void getAllGroup() {
        loadingViewEnable();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpBase.getRecommendApi)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        Call<JsonObject> call = retrofitService.getAllGroup();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loadingViewDisable();
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Parse(response.body().toString());
                    }
                } else {
                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadingViewDisable();
            }
        });
    }

    /**
     * 處理api回傳的資料
     * */
    private void Parse(String jsonObject) {
        try {
            JSONObject object = new JSONObject(jsonObject);
            int count = object.getJSONArray("data").length();
            int total = object.getInt("total");

            for (int index = 0; index < count; index++) {

                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> num = new ArrayList<>();
                String VF = object.getJSONArray("data").getJSONObject(index).getString("VF");
                String unit = object.getJSONArray("data").getJSONObject(index).getString("Units");

                JSONObject company_name = object.getJSONArray("data").getJSONObject(index);
                for (int size = 0; size < company_name.getJSONArray("company_name").length(); size++) {
                    name.add(company_name.getJSONArray("company_name").getString(size));
                    num.add(company_name.getJSONArray("stock_symbol").getString(size));
                }
                RecommendPost post = new RecommendPost(name, num, VF, unit, String.valueOf(total));
                data.add(post);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 激活recyclerView
        adapter = new RecommendAdapter(data, requireActivity());
        recyclerView.setAdapter(adapter);
    }

    /**
     * 百分比運算
     * */
    public double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void loadingViewEnable(){
        progressView.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);

    }

    private void loadingViewDisable(){
        progressView.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.VISIBLE);
    }
}
