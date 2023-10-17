package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.SubjectStats;
import com.youth4work.prepapp.ui.views.CustomTextViewFontRegular;


import butterknife.BindView;
import butterknife.ButterKnife;


public class SubjectStatItem extends AbstractItem<SubjectStatItem, SubjectStatItem.ViewHolder> {
    public SubjectStats mSubjectStats;

    public SubjectStatItem(SubjectStats subjectStats) {
        mSubjectStats = subjectStats;
    }

    @Override
    public int getType() {
        return R.id.subject_stats_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_subject_stat;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        viewHolder.statProgressBar.setMax(100);
        viewHolder.statRedProgressBar.setMax(100);
        viewHolder.statYellowProgressBar.setMax(100);

        viewHolder.statProgressBar.setProgressWithAnim((int) mSubjectStats.getScore());
        viewHolder.statRedProgressBar.setProgressWithAnim((int) mSubjectStats.getScore());
        viewHolder.statYellowProgressBar.setProgressWithAnim((int) mSubjectStats.getScore());

        viewHolder.txtSubjectName.setText(mSubjectStats.getSubject());
        viewHolder.txtSubjectScore.setText((int) mSubjectStats.getScore() + "");


        if(mSubjectStats.getScore() < 30) {
            viewHolder.statRedProgressBar.setVisibility(View.VISIBLE);
            viewHolder.statProgressBar.setVisibility(View.GONE);
            viewHolder.statYellowProgressBar.setVisibility(View.GONE);
        } else if (mSubjectStats.getScore() > 30 && mSubjectStats.getScore() < 50 ) {
            viewHolder.statYellowProgressBar.setVisibility(View.VISIBLE);
            viewHolder.statProgressBar.setVisibility(View.GONE);
            viewHolder.statRedProgressBar.setVisibility(View.GONE);
        } else if(mSubjectStats.getScore() > 50) {
            viewHolder.statProgressBar.setVisibility(View.VISIBLE);
            viewHolder.statRedProgressBar.setVisibility(View.GONE);
            viewHolder.statYellowProgressBar.setVisibility(View.GONE);
        }

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_subject_name)
        CustomTextViewFontRegular txtSubjectName;
        @BindView(R.id.stat_progress_bar)
        AnimateHorizontalProgressBar statProgressBar;
        @BindView(R.id.stat_red_progress_bar)
        AnimateHorizontalProgressBar statRedProgressBar;
        @BindView(R.id.stat_yellow_progress_bar)
        AnimateHorizontalProgressBar statYellowProgressBar;
        @BindView(R.id.txt_subject_score)
        TextView txtSubjectScore;
        @BindView(R.id.cv)
        CardView cv;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}