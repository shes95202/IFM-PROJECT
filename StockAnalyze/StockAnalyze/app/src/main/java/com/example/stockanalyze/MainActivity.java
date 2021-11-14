package com.example.stockanalyze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stockanalyze.Fragment.Hotspots;
import com.example.stockanalyze.Fragment.MyGroup;
import com.example.stockanalyze.Fragment.Recommend;
import com.example.stockanalyze.Fragment.Search;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationHost{

    SystemStyle systemStyle = new SystemStyle();

    InputMethodManager inputMethodManager;
    LinearLayout bottomLinearLayout;
    TextView item0,item1,item2,item3;
    String status = "";
    List<TextView> bottomTexts = new ArrayList<>();

    final private String Hotspots = "Hotspots";
    final private String Search = "Search";
    final private String Recommend = "Recommend";
    final private String MyGroup = "MyGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        systemStyle.getScreenSize(this);
        systemStyle.setTitleBarColor(this);
        systemStyle.systemTextDark(this);

        initLayout();
        createBottomTextArray();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment, new Hotspots())
                    .commitAllowingStateLoss();
            status = Hotspots;
        }
    }

    /**
     * 初始化元件
     * */
    private void initLayout(){
        bottomLinearLayout = findViewById(R.id.linearLayout);
        item0 = findViewById(R.id.item0);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);

        item0.setOnClickListener(this);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
    }

    /**
     * 將下方四個按鈕添加到array
     * */
    private void createBottomTextArray(){
        bottomTexts = new ArrayList<>();
        bottomTexts.add(item0);
        bottomTexts.add(item1);
        bottomTexts.add(item2);
        bottomTexts.add(item3);
    }

    /**
     * 下方四個按鈕監聽
     * */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item0:
                if(!status.equals(Hotspots))
                    changeBottomTextColor(item0);
                    NavigateTo(new Hotspots(),"Hotspots");
                break;

            case R.id.item1:
                if(!status.equals(Search))
                    changeBottomTextColor(item1);
                    NavigateTo(new Search(),"Search");
                break;

            case R.id.item2:
                if(!status.equals(Recommend))
                    changeBottomTextColor(item2);
                    NavigateTo(new Recommend(),"Recommend");
                break;

            case R.id.item3:
                if(!status.equals(MyGroup))
                    changeBottomTextColor(item3);
                    NavigateTo(new MyGroup(),"MyGroup");

                break;
        }
    }

    /**
     * 點擊空白處收起鍵盤
     * */
    public void onClicks(View v) {
        if (null == inputMethodManager) {
            inputMethodManager = (InputMethodManager)
                    this.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void NavigateTo(Fragment fragment, String tag) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_fragment, fragment);

        status = tag;

        transaction.commitAllowingStateLoss();
    }

    /**
     * 修下方四個按鈕的文字顏色
     * */
    private void changeBottomTextColor(TextView textView){
        for (int i=0;i<bottomTexts.size();i++){
            if(textView == bottomTexts.get(i)){
                textView.setTextColor(getResources().getColor(R.color.textSelect));
            }else{
                bottomTexts.get(i).setTextColor(getResources().getColor(R.color.textUnSelect));
            }
        }
    }

    /**
     * 取消返回功能
     * */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}