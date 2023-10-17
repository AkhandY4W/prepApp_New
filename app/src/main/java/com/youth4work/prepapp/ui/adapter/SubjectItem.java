package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Subject;

public class SubjectItem extends AbstractItem<SubjectItem, SubjectItem.ViewHolder> {
    public Subject mSubject;
    Context mContext;

    public SubjectItem(Subject subject, Context context) {
        mSubject = subject;
        mContext=context;
    }

    @Override
    public int getType() {
        return R.id.subject_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_section_test;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        viewHolder.txtSub.setText(mSubject.getTestname());
        viewHolder.txtAsp.setText(mSubject.getTested()>1000?mSubject.getTested()/1000+"k Aspirants":mSubject.getTested()+" Aspirants");

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardCat;
        TextView txtSub,txtAsp,txtTakeTest;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCat=itemView.findViewById(R.id.card_cat);
            txtAsp=itemView.findViewById(R.id.txt_asp);
            txtSub=itemView.findViewById(R.id.txt_cat_name);
            txtTakeTest=itemView.findViewById(R.id.txt_take_test);

        }
    }
}