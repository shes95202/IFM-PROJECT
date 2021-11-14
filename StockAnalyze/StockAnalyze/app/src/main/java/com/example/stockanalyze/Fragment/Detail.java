package com.example.stockanalyze.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.stockanalyze.Http.HttpBase;
import com.example.stockanalyze.NavigationHost;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.Detail.DetailPost;
import com.example.stockanalyze.Retrofit.RetrofitService;
import com.example.stockanalyze.SystemStyle;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
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

public class Detail extends Fragment {

    final private String Hotspots = "Hotspots";
    final private String Search = "Search";
    final private String Recommend = "Recommend";
    final private String MyGroup = "MyGroup";

    SystemStyle systemStyle = new SystemStyle();
    HttpBase httpBase = new HttpBase();

    RetrofitService retrofitService;
    DetailPost data;

    ConstraintLayout progressView;
    ImageView backBtn;
    TextView nameText, numText;
    TextView upText,downText,noneText;
    TabLayout tabLayout;

    String stockName = "";
    String stockNum = "";
    String backView = "";

    /**
     * 初始化元件
     * */
    private void initLayout(View view) {

        backBtn = view.findViewById(R.id.detail_back_btn);
        nameText = view.findViewById(R.id.detail_name);
        numText = view.findViewById(R.id.detail_num);
        upText = view.findViewById(R.id.upText);
        downText = view.findViewById(R.id.downText);
        noneText = view.findViewById(R.id.noneText);
        tabLayout = view.findViewById(R.id.tabLayout);
        progressView = view.findViewById(R.id.detail_view);

        // tabView 監聽
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("短線")) {
                    setShortText();
                } else {
                    setLongText();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // 返回鍵間聽
        backBtn.setOnClickListener(v -> {
            switch (backView) {
                case Hotspots:
                    ((NavigationHost) requireActivity()).NavigateTo(new Hotspots(), "Hotspots");
                    break;
                case Search:
                    ((NavigationHost) requireActivity()).NavigateTo(new Search(), "Search");
                    break;
                case Recommend:
                    ((NavigationHost) requireActivity()).NavigateTo(new Recommend(), "Recommend");
                    break;
                case MyGroup:
                    ((NavigationHost) requireActivity()).NavigateTo(new MyGroup(), "MyGroup");
                    break;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_detail, container, false);

        systemStyle.getScreenSize(requireActivity());
        systemStyle.setTitleBarColor(requireActivity());
        initLayout(view);
        getBundle();

        return view;
    }

    /**
     * 取得前一個fragment 傳遞過來的消息
     * */
    private void getBundle() {
        requireActivity().getSupportFragmentManager().setFragmentResultListener("detail", this, (requestKey, result) -> {
            stockName = result.getString("name");
            stockNum = result.getString("num");
            backView = result.getString("backView");

            nameText.setText(stockName);
            numText.setText(stockNum);

            getStockDetail();
        });
    }

    /**
     * call api 取得股票詳細資料
     * */
    private void getStockDetail() {
        progressView.setVisibility(View.VISIBLE);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpBase.urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        Call<JsonObject> call = retrofitService.getStockDetail(stockName);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressView.setVisibility(View.INVISIBLE);
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
                progressView.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 處理api回傳的資料
     * */
    private void Parse(String jsonObject) {

        final String shortLine = "短線";
        final String longLine = "長線";
        final String up = "上漲";
        final String down = "下跌";
        final String none = "無影響";

        ArrayList<String> sUps = new ArrayList<>();
        ArrayList<String> sDowns= new ArrayList<>();
        ArrayList<String> sNones= new ArrayList<>();
        ArrayList<String> lUps= new ArrayList<>();
        ArrayList<String> lDowns= new ArrayList<>();
        ArrayList<String> lNones= new ArrayList<>();

        try {
            JSONObject object = new JSONObject(jsonObject);

            JSONObject shorts = object.getJSONArray("data").getJSONObject(0);
            if (shorts.has(shortLine)) {
                JSONObject value = shorts.getJSONObject(shortLine);
                sUps = getParseData(value.getJSONArray(up).length(), value.getJSONArray(up));
                sDowns = getParseData(value.getJSONArray(down).length(), value.getJSONArray(down));
                sNones = getParseData(value.getJSONArray(none).length(), value.getJSONArray(none));
            }

            JSONObject longs = object.getJSONArray("data").getJSONObject(1);
            if (longs.has(longLine)) {
                JSONObject value = longs.getJSONObject(longLine);
                lUps = getParseData(value.getJSONArray(up).length(), value.getJSONArray(up));
                lDowns = getParseData(value.getJSONArray(down).length(), value.getJSONArray(down));
                lNones = getParseData(value.getJSONArray(none).length(), value.getJSONArray(none));
            }

            data = new DetailPost(sUps,sDowns,sNones,lUps,lDowns,lNones);

            if (tabLayout.getSelectedTabPosition() == 0){
                setShortText();
            }else{
                setLongText();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 設置短線資料
     * */
    private void setShortText(){
        setScrollText(data.shortUps,upText);
        setScrollText(data.shortDowns,downText);
        setScrollText(data.shortNones,noneText);
    }

    /**
     * 設置長線資料
     * */
    private void setLongText(){
        setScrollText(data.longUps,upText);
        setScrollText(data.longDowns,downText);
        setScrollText(data.longNones,noneText);
    }

    private ArrayList<String> getParseData(int arrayCount, JSONArray jsonData) {
        ArrayList<String> array = new ArrayList<>();

        try {
            for (int index = 0; index < arrayCount; index++) {
                array.add(jsonData.getString(index));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * 設置長線/短線中 ， 上漲、下跌、無影顯得資訊到畫面上
     * */
    private void setScrollText(ArrayList<String> text, TextView textView){
        String allText = "";
        for(int index=0;index<text.size();index++){
            if (!text.get(index).equals("null")){
                if(index==text.size()-1){
                    allText = allText + text.get(index);
                }else{
                    allText = allText + text.get(index)+"、";
                }
            }else{
                allText = "尚無此資料";
            }
        }
        textView.setText(allText);
    }
}
