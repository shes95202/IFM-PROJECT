package com.example.stockanalyze.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.stockanalyze.Fragment.MyGroup;
import com.example.stockanalyze.Fragment.Recommend;
import com.example.stockanalyze.Http.HttpBase;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.Hotspots.HotspotsPost;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupAdapter;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupPost;
import com.example.stockanalyze.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDialog extends Dialog {
    HttpBase httpBase = new HttpBase();
    RetrofitService retrofitService;

    Activity activity;
    ConstraintLayout view;
    EditText edit;
    Spinner spinner;
    Button addBtn, cancelBtn;

    ArrayList<StockData> data = new ArrayList<>();
    ArrayList<HotspotsPost> allStocks;
    int screenWeight;
    int screenHeight;
    String selectName = "";
    String selectNum = "";
    MyGroupAdapter adapter;


    public AddDialog(Activity activity, int W, int H,MyGroupAdapter adapter) {
        super(activity, R.style.CustomDialog);
        this.activity = activity;
        this.screenWeight = W;
        this.screenHeight = H;
        this.adapter = adapter;
    }

    /**
     * 初始化元件
     * */
    private void initLayout() {

        view = findViewById(R.id.add_view);
        edit = findViewById(R.id.add_edit);
        spinner = findViewById(R.id.add_spinner);
        addBtn = findViewById(R.id.add_ok);
        cancelBtn = findViewById(R.id.add_cancel);

        view.getLayoutParams().width = (int) (screenWeight * 0.9);
        edit.getLayoutParams().width = (int) (screenWeight * 0.3);

        addBtn.setOnClickListener(v -> {
            if (!edit.getText().toString().equals("")) {
                Recommend.groupData.add(new MyGroupPost(
                        data.get(spinner.getSelectedItemPosition()).Name,
                        data.get(spinner.getSelectedItemPosition()).Num,
                        edit.getText().toString()
                        ));
                adapter.values.add(edit.getText().toString());
                adapter.notifyItemInserted(Recommend.groupData.size()-1);
                MyGroup.reloadTotal();
                dismiss();
            } else {
                Toast.makeText(getContext(), "請輸入百分比！", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add);

        initLayout();
        getAllStock();
    }

    /**
     * 初始化下拉選單
     * */
    private void initSpinner() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        StockAdapter adapter = new StockAdapter(getContext(), data);
        spinner.setAdapter(adapter);
    }

    /**
     * 創建下拉選單的資料集
     * */
    private void getNoneUsedStock() {
        for (int index = 0; index < allStocks.size(); index++) {

            boolean isSelect = false;

            for (int groupData = 0; groupData < Recommend.groupData.size(); groupData++) {
                if (allStocks.get(index).name.equals(Recommend.groupData.get(groupData).name)) {
                    isSelect = true;
                }
            }
            if (!isSelect) {
                data.add(new StockData(allStocks.get(index).name, allStocks.get(index).num));
            }
        }
    }

    /**
     * call api 取得所有股票
     * */
    private void getAllStock() {
        allStocks = new ArrayList<>();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(httpBase.getSearchStock)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        Call<JsonObject> call = retrofitService.getAllGroup();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Parse(response.body().toString());
                    }
                } else {
                    Toast.makeText(getContext(), "股票列表獲取失敗", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
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

            for (int index = 0; index < count; index++) {

                String name = object.getJSONArray("data").getJSONObject(index).getString("company_name");

                if (!name.equals("null")) {
                    String num = object.getJSONArray("data").getJSONObject(index).getString("stock_symbol");
                    String shortLine = object.getJSONArray("data").getJSONObject(index).getString("short_profit");
                    String longLine = object.getJSONArray("data").getJSONObject(index).getString("long_profit");

                    HotspotsPost post = new HotspotsPost(name, num, shortLine, longLine);
                    allStocks.add(post);
                }
            }

            getNoneUsedStock();
            initSpinner();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下拉選單監聽
     * */
    private class StockAdapter extends ArrayAdapter {

        public StockAdapter(@NonNull Context context, @NonNull List<StockData> mData) {
            super(context, 0, mData);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createView(position, convertView, parent, false);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createView(position, convertView, parent, true);
        }

        private View createView(int position, View convertView, ViewGroup parent, Boolean ageDisplay) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item, parent, false);
            TextView location_text = convertView.findViewById(R.id.item_location_text);
            StockData item = (StockData) getItem(position);
            if (item != null) {
                location_text.setText(item.getName());
                selectName = item.getName();
                selectNum = item.getNum();
            }
            return convertView;
        }
    }

    /**
     * 下拉選單資料集的型別
     * */
    class StockData {

        private String Name;
        private String Num;

        public StockData(String name, String num) {
            this.Name = name;
            this.Num = num;
        }

        public String getName() {
            return Name;
        }

        public String getNum() {
            return Num;
        }
    }
}
