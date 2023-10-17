package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.UserStats;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserStatsItem extends AbstractItem<UserStatsItem, UserStatsItem.ViewHolder> {
    public UserStats mUserStats;

    public UserStatsItem(UserStats userStats) {
        mUserStats = userStats;
    }

    @Override
    public int getType() {
        return R.id.user_stats_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_user_stats;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        viewHolder.speedCircleView.setProgress((int)Math.round(mUserStats.getSpeed()));
        viewHolder.accuracyCircleView.setProgress((int)Math.round(mUserStats.getAcuracy()));
        viewHolder.scoreCircleView.setProgress((int)Math.round(mUserStats.getScore()));
        viewHolder.speedCircleView.setArcAngle(270F);
        viewHolder.accuracyCircleView.setArcAngle(270F);
        viewHolder.scoreCircleView.setArcAngle(270F);
        viewHolder.txtAccuracy.setText((Math.round(mUserStats.getAcuracy()*100))/100+"%");
        viewHolder.txtScore.setText(Double.toString(mUserStats.getScore()));
        viewHolder.txtSpeed.setText(Double.toString(mUserStats.getSpeed()));
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.accuracyCircleView)
        ArcProgress accuracyCircleView;
        @BindView(R.id.scoreCircleView)
        ArcProgress scoreCircleView;
        @BindView(R.id.speedCircleView)
        ArcProgress speedCircleView;
        @BindView(R.id.txt_speed)
        TextView txtSpeed;
        @BindView(R.id.txt_accuracy)
        TextView txtAccuracy;
        @BindView(R.id.txt_score)
        TextView txtScore;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}