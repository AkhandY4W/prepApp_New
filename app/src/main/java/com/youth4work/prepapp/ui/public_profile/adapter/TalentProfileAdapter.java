package com.youth4work.prepapp.ui.public_profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.response.user_profile.TalentProfileModel;
import com.youth4work.prepapp.util.Constants;

import java.util.ArrayList;



public class TalentProfileAdapter extends RecyclerView.Adapter<TalentProfileAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<TalentProfileModel> mTalentProfileModel;

    public TalentProfileAdapter() {

    }

    public TalentProfileAdapter(Context mContext, ArrayList<TalentProfileModel> mTalentProfileModel) {
        this.mContext = mContext;
        this.mTalentProfileModel = mTalentProfileModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_talent_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtTalent.setText(mTalentProfileModel.get(i).getTalent());
        if (mTalentProfileModel.get(i).getTotalPoint() == 0) {
            viewHolder.txtReputationPoint.setVisibility(View.GONE);
            viewHolder.view2.setVisibility(View.GONE);

        } else {
            viewHolder.txtReputationPoint.setText(mTalentProfileModel.get(i).getTotalPoint() + " yRepo");
        }
        if (mTalentProfileModel.get(i).getyRank() == 0) {

            viewHolder.txtGlobalyRank.setVisibility(View.GONE);
            viewHolder.view2.setVisibility(View.GONE);
        } else {
            viewHolder.txtGlobalyRank.setText(mTalentProfileModel.get(i).getyRank() + " yRank");
        }
        if (mTalentProfileModel.get(i).getScore() == 0) {
            viewHolder.txtyScore.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.GONE);
        } else {
            viewHolder.txtyScore.setText(Constants.getFloat2digit(mTalentProfileModel.get(i).getScore()) + " yScore");
        }
        if (mTalentProfileModel.get(i).getTotalYouth() == 0) {
            viewHolder.txtCityRank.setVisibility(View.GONE);
        } else {
            viewHolder.txtCityRank.setText("5/" + mTalentProfileModel.get(i).getTotalYouth() + " yRank In Delhi");
        }


    }

    @Override
    public int getItemCount() {
        return mTalentProfileModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTalent, txtCityRank, txtyScore,
                txtReputationPoint, txtGlobalyRank;
        View view1, view2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTalent = itemView.findViewById(R.id.txt_talent);
            txtCityRank = itemView.findViewById(R.id.txt_city_rank);
            txtyScore = itemView.findViewById(R.id.txt_yscore);
            txtGlobalyRank = itemView.findViewById(R.id.txt_global_yrank);
            txtReputationPoint = itemView.findViewById(R.id.txt_reputation_point);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
        }
    }
}
