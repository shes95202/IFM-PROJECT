package com.example.stockanalyze.RecyclerView.MyGroup;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.R;

import java.util.ArrayList;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.ExampleViewHolder>{

    ArrayList<MyGroupPost> mData;
    public ArrayList<String> values;
    Activity mActivity;

    ExampleViewHolder holder;
    MyGroupAdapter.OnItemClickListener mListener;

    public MyGroupAdapter(ArrayList<MyGroupPost> data, Activity activity) {
        mData = data;
        this.mActivity = activity;
        values = new ArrayList<>();

        if(mData==null){
            values.add("0");
        }else{
            for(int i=0;i<mData.size();i++){
                values.add(mData.get(i).percent);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int index);

        void toDetail(int index);
    }

    public void setOnItemClickListener(MyGroupAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_group, parent, false);
        holder = new ExampleViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        final MyGroupPost post = mData.get(position);

        holder.nameText.setText(post.name);
        holder.numText.setText(post.num);
        holder.percentEdit.setText(post.percent);

        // 文字監聽器，只取得修改後的內容
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    values.set(position, "0");
                } else {
                    values.set(position, s.toString());
                }
            }
        };

        // 輸入匡監聽，輸入匡取得焦點時，則把文字監聽器新增到自己本身，反之則移除
        holder.percentEdit.setOnFocusChangeListener((view, b) -> {
            if(b){
                holder.percentEdit.addTextChangedListener(textWatcher);
            }else{
                holder.percentEdit.removeTextChangedListener(textWatcher);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mData==null){
            return 0;
        }else{
            return mData.size();
        }
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView nameText,numText;
        ImageView deleteBtn;
        EditText percentEdit;

        public ExampleViewHolder(@NonNull View itemView, MyGroupAdapter.OnItemClickListener listener) {
            super(itemView);

            nameText = itemView.findViewById(R.id.cell_my_name);
            numText = itemView.findViewById(R.id.cell_my_num);
            percentEdit = itemView.findViewById(R.id.cell_my_percent);
            deleteBtn = itemView.findViewById(R.id.cell_my_delete_btn);

            // 刪除按鈕點擊監聽
            deleteBtn.setOnClickListener(view -> {
                if (listener != null) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            // 股票名稱點擊監聽
            nameText.setOnClickListener(view -> {
                if (listener != null) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.toDetail(getAdapterPosition());
                    }
                }
            });

            // 股票代碼點擊監聽
            numText.setOnClickListener(view -> {
                if (listener != null) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.toDetail(getAdapterPosition());
                    }
                }
            });

        }
    }

    public ArrayList<String> getValues(){
        return values;
    }

}
