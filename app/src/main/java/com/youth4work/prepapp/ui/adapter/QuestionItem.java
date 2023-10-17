package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Question;


public class QuestionItem extends AbstractItem<QuestionItem, QuestionItem.ViewHolder> {
    public Question mQuestion;

    public QuestionItem(Question question) {
        mQuestion = question;
    }

    @Override
    public int getType() {
        return R.id.question_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_discussion;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        viewHolder.txtQuestion.setText("\n"+Html.fromHtml(mQuestion.getQuestion()));
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        LinearLayout lytOptionContainer;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            lytOptionContainer = itemView.findViewById(R.id.linearlayout);
            txtQuestion = itemView.findViewById(R.id.que);
        }
    }
}