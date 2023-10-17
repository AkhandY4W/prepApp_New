package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.PrepForum;
import com.youth4work.prepapp.util.Constants;

/**
 * Created by Samar on 5/12/2016.
 */
public class PrepForumItem extends AbstractItem<PrepForumItem, PrepForumItem.ViewHolder> {
    public PrepForum prepForum;
    Context context;

    public PrepForumItem(PrepForum prepForum, Context context) {
        this.prepForum = prepForum;
        this.context = context;
    }

    @Override
    public int getType() {
        return R.id.attempt_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_forum_que_ans;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        viewHolder.txtForum.setText(prepForum.getTitle());
        viewHolder.txtTitle.setText(prepForum.getForum());
        String text = "By "+Constants.getFirstName(prepForum.getCreatedByName())+" • "+prepForum.getTotalView()+" View • "+prepForum.getTotalAnswer()+" Answer";
        viewHolder.txtCreatedByName.setText(text);
        //viewHolder.txtTotalView.setText("View : " + Integer.toString(prepForum.getTotalView()));
        // viewHolder.txtTotalAnswers.setText("Answers : " + Integer.toString(prepForum.getTotalAnswer()));
        Picasso.get().load(prepForum.getCreatedByPic()).into(viewHolder.imageView);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtForum;
        TextView txtTitle;
        TextView txtCreatedByName;
        /*TextView txtTotalAnswers;
        TextView txtTotalView;*/
        CircularImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtForum = (TextView) itemView.findViewById(R.id.forum);
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            txtCreatedByName = (TextView) itemView.findViewById(R.id.createdByName);
            //txtTotalAnswers = (TextView) itemView.findViewById(R.id.totalAnswer);
            // txtTotalView = (TextView) itemView.findViewById(R.id.totalViews);
            imageView = (CircularImageView) itemView.findViewById(R.id.imageQuestion);

        }
    }
}



