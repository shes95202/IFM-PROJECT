package com.example.stockanalyze.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.stockanalyze.Fragment.MyGroup;
import com.example.stockanalyze.Fragment.Recommend;
import com.example.stockanalyze.MainActivity;
import com.example.stockanalyze.R;
import com.example.stockanalyze.RecyclerView.MyGroup.MyGroupAdapter;

public class DeletedDialog extends Dialog {

    Activity activity;
    int screenWeight;
    int position;
    MyGroupAdapter adapter;

    Button okBtn,cancelBtn;
    ConstraintLayout view;

    public DeletedDialog(Activity activity, int W , int position, MyGroupAdapter adapter) {
        super(activity, R.style.CustomDialog);

        this.activity = activity;
        this.screenWeight = W;
        this.position = position;
        this.adapter = adapter;
    }

    /**
     * 初始化元件
     * */
    private void initLayout(){
        view = findViewById(R.id.delete_view);
        okBtn = findViewById(R.id.delete_ok);
        cancelBtn = findViewById(R.id.delete_cancel);

        view.getLayoutParams().width = (int)(screenWeight * 0.9);

        okBtn.setOnClickListener(v ->{
            Recommend.groupData.remove(position);
            adapter.values.remove(position);
            adapter.notifyItemRemoved(position);
            MyGroup.reloadTotal();
            dismiss();
        });

        cancelBtn.setOnClickListener(v ->{
            dismiss();
        });
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_deleted);

        initLayout();
    }
}
