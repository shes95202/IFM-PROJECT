package com.example.stockanalyze.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.Dialog.AddDialog;
import com.example.stockanalyze.Dialog.DeletedDialog;
import com.example.stockanalyze.NavigationHost;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.Hotspots.HotspotsPost;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupAdapter;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupPost;
import com.example.stockanalyze.SystemStyle;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MyGroup extends Fragment {

    SystemStyle systemStyle = new SystemStyle();

    public static MyGroupAdapter adapter;

    RecyclerView recyclerView;
    public static TextView totalText;
    public static Button addBtn,reloadBtn;

    /**
     * 初始化元件
     * */
    private void initLayout(View view) {

        recyclerView = view.findViewById(R.id.my_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        totalText = view.findViewById(R.id.my_totalText);
        addBtn = view.findViewById(R.id.my_btn);
        reloadBtn = view.findViewById(R.id.my_reload_btn);

        // 激活recyclerView
        adapter = new MyGroupAdapter(Recommend.groupData, requireActivity());
        recyclerView.setAdapter(adapter);

        // recyclerView 點擊監聽
        adapter.setOnItemClickListener(new MyGroupAdapter.OnItemClickListener() {

            // 刪除
            @Override
            public void onItemClick(int index) {
                DeletedDialog dialog = new DeletedDialog(requireActivity(),systemStyle.getWidth(),index,adapter);
                dialog.show();
            }

            // detail
            @Override
            public void toDetail(int index) {

                Bundle bundle = new Bundle();
                bundle.putString("name",Recommend.groupData.get(index).name);
                bundle.putString("num",Recommend.groupData.get(index).num);
                bundle.putString("backView","MyGroup");
                requireActivity().getSupportFragmentManager().setFragmentResult("detail",bundle);

                ((NavigationHost)requireActivity()).NavigateTo(new Detail(),"Detail");
            }
        });

        // 新增按鈕 點擊監聽
        addBtn.setOnClickListener(v -> {
            AddDialog add = new AddDialog(requireActivity(), systemStyle.getWidth(),systemStyle.getHeight(),adapter);
            add.show();
        });

        // 更新按鈕 點擊監聽
        reloadBtn.setOnClickListener(v -> {
            reloadTotal();
        });
    }

    /**
     * 重新取得修改百分比後的總百分比
     * */
    public static void reloadTotal() {
        float total = 0;
        ArrayList<String> data = adapter.getValues();
        for (int index = 0; index < data.size(); index++) {
            if (data.get(index).equals("")) {
                total = total + 0;
            } else {

                Recommend.groupData.set(index,new MyGroupPost(
                        Recommend.groupData.get(index).name,
                        Recommend.groupData.get(index).num,
                        data.get(index)
                ));
                adapter.notifyItemChanged(index);
                total = total + Float.parseFloat(data.get(index));
            }
        }
        BigDecimal value=   new   BigDecimal(total);
        total = value.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();

        if (total!=0){
            totalText.setText("" + total+"%");
            Visible();
        }else{
            Invisible();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_group, container, false);

        systemStyle.getScreenSize(requireActivity());
        systemStyle.setMyTitleBarColor(requireActivity());

        initLayout(view);

        // 判斷是否有按下確定組合
        if (Recommend.groupData == null) {
            Invisible();
        } else {
            reloadTotal();
        }

        return view;
    }

    /**
     * 有按下確定組合，才會顯示新增按鈕、更新按鈕
     * */
    private static void Visible() {
        addBtn.setVisibility(View.VISIBLE);
        reloadBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 沒按下確定組合，則隱藏新增按鈕、更新按鈕
     * 總百分比為0，也隱藏新增按鈕、更新按鈕
     * */
    private static void Invisible() {
        addBtn.setVisibility(View.INVISIBLE);
        reloadBtn.setVisibility(View.INVISIBLE);
        totalText.setText("請先組合");
    }
}
