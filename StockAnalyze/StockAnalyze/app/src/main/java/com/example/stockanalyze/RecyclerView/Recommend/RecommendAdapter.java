package com.example.stockanalyze.RecyclerView.Recommend;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockanalyze.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ExampleViewHolder> {

    ArrayList<RecommendPost> mData;
    Activity mActivity;
    ExampleViewHolder holder;
    ArrayList<Integer> indexes ;

    public RecommendAdapter(ArrayList<RecommendPost> data, Activity activity) {
        mData = data;
        this.mActivity = activity;

        indexes = new ArrayList<>();
        for(int i=0;i<mData.size();i++){
            indexes.add(0);
        }
    }

    @NonNull
    @Override
    public RecommendAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_recommend, parent, false);
        holder = new ExampleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendAdapter.ExampleViewHolder holder, int position) {
        final RecommendPost post = mData.get(position);

        // 判斷api 回傳是否回空回傳
        if (!post.name.get(0).equals("null")) {
            double percent = div(Double.parseDouble(post.unit) * 100, Double.parseDouble(post.total) * 100, 3);

            holder.nameText.setText(post.name.get(0));
            holder.numText.setText(post.num.get(0));

            // 判斷取得最後一筆資料時，將最後一筆資料的百分比修改成符合100%
            if(position == mData.size()-1){
                double allOfBeforeValue = 0;
                for(int i=0;i<mData.size()-1;i++){
                    allOfBeforeValue = allOfBeforeValue + div(Double.parseDouble(mData.get(i).unit) * 100, Double.parseDouble(mData.get(i).total) * 100, 3);
                }
                float endValue = (float) (100 - allOfBeforeValue*100);
                holder.percentText.setText(endValue+"%");
            }else{
                holder.percentText.setText(((float) percent) * 100 + "%");
            }

            holder.constraintLayout.setOnClickListener(view -> {

            });

            // 更換按鈕監聽
            // indexes 是儲存當前各個group所顯示的資料所在index
            holder.changeBtn.setOnClickListener(view -> {
                for (int row = 0; row < post.name.size(); row++) {
                    if (holder.nameText.getText().equals(post.name.get(row))) {
                        int indexOf = post.name.indexOf(holder.nameText.getText());

                        if ( indexOf+1 < post.name.size()) {
                            holder.nameText.setText(post.name.get(indexOf+1));
                            holder.numText.setText(post.num.get(indexOf+1));

                            indexes.set(position,indexOf+1);
                        }else if (indexOf+1 == post.name.size()){
                            holder.nameText.setText(post.name.get(0));
                            holder.numText.setText(post.num.get(0));

                            indexes.set(position,0);
                        }
                        return;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView nameText, numText, percentText;
        Button changeBtn;
        ConstraintLayout constraintLayout;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.cell_recommend_name);
            numText = itemView.findViewById(R.id.cell_recommend_num);
            percentText = itemView.findViewById(R.id.cell_recommend_percent);
            changeBtn = itemView.findViewById(R.id.cell_recommend_btn);
            constraintLayout = itemView.findViewById(R.id.recommend_cons);

        }
    }

    /**
     * 取得百分比運算
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

    public ArrayList<Integer> getNowViewData(){
        return  indexes;
    }
}
