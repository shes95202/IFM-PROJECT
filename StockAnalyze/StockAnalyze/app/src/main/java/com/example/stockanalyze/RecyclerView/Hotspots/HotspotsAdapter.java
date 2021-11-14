package com.example.stockanalyze.RecyclerView.Hotspots;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.Fragment.Hotspots;
import com.example.stockanalyze.NavigationHost;
import com.example.stockanalyze.R;

import java.util.ArrayList;

public class HotspotsAdapter extends RecyclerView.Adapter<HotspotsAdapter.ExampleViewHolder> {

    ArrayList<HotspotsPost> mData;
    Activity mActivity;

    ExampleViewHolder holder;
    HotspotsAdapter.OnItemClickListener mListener;

    final private String UP = "上漲";
    final private String DOWN = "下跌";
    final private String NOT = "無影響";

    public HotspotsAdapter(ArrayList<HotspotsPost> data, Activity activity) {
        mData = data;
        this.mActivity = activity;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(HotspotsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_hotspots, parent, false);
        holder = new ExampleViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotspotsAdapter.ExampleViewHolder holder, int position) {
        final HotspotsPost post = mData.get(position);

        if (!post.name.equals("null")) {
            holder.nameText.setText(post.name);
            holder.numText.setText(post.num);
            holder.shortText.setText(post.shortLine);
            holder.longText.setText(post.longLine);

            changeTextColor(holder.shortText, post.shortLine);
            changeTextColor(holder.longText, post.longLine);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView nameText, numText, shortText,longText;
        ConstraintLayout constraintLayout;

        public ExampleViewHolder(@NonNull View itemView, HotspotsAdapter.OnItemClickListener listener) {
            super(itemView);

            nameText = itemView.findViewById(R.id.cell_hotspots_name);
            numText = itemView.findViewById(R.id.cell_hotspots_num);
            shortText = itemView.findViewById(R.id.cell_hotspots_shortLine);
            longText = itemView.findViewById(R.id.cell_hotspots_longLine);
            constraintLayout = itemView.findViewById(R.id.cell_hotspots_view);

            // 整條view 監聽，點擊即可前往detail
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }

    /**
     * 文字顏色修改
     * */
    private void changeTextColor(TextView tv, String type){
        switch (type){
            case UP:
                tv.setTextColor(mActivity.getResources().getColor(R.color.red));
                break;
            case DOWN:
                tv.setTextColor(mActivity.getResources().getColor(R.color.green));
                break;
            case NOT:
                tv.setTextColor(mActivity.getResources().getColor(R.color.darkGray));
                break;
        }
    }
}