package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Comments;
import com.youth4work.prepapp.util.Constants;

import java.util.Calendar;


public class DiscussionItem extends AbstractItem<DiscussionItem, DiscussionItem.ViewHolder> {
    private Comments.Comment mComment;

    public DiscussionItem(Comments.Comment comment) {
        mComment = comment;
    }

    @Override
    public int getType() {
        return R.id.discussion_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_comment;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        //String commentedTime = mComment.getLastModified();
      //  commentedTime = commentedTime.replace("/Date(", "");
      //  commentedTime = commentedTime.replace("+0530)/", "");

       // Calendar calendar = Calendar.getInstance();
       // calendar.setTimeInMillis(Long.parseLong(commentedTime));
        viewHolder.txtComment.setText(mComment.getComment());
        String text = "By " + Constants.getFirstName(mComment.getCommentByName());
        viewHolder.txtCommentUsername.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        //viewHolder.txtCommentUsername.setText(mComment.getCommentByName());
        //viewHolder.txtCommentDateTime.setText(timeAgo.timeAgo(calendar.getTime()));
        Picasso.get().load(mComment.getCommentByPic()).placeholder(R.drawable.ic_email_id_and_username).into(viewHolder.imgCommentUserAvatar);
        if (mComment.getCommentImage() != null && !mComment.getCommentImage().equals("")) {
            viewHolder.commentImage.setVisibility(View.VISIBLE);
            Picasso.get().load(mComment.getCommentImage()).into(viewHolder.commentImage);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtComment;
        TextView txtCommentUsername;
        TextView txtCommentDateTime;
        ImageView imgCommentUserAvatar, commentImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.txt_comment);
            txtCommentUsername = itemView.findViewById(R.id.txt_commented_user);
            txtCommentDateTime = itemView.findViewById(R.id.txt_comment_date_time);
            imgCommentUserAvatar = itemView.findViewById(R.id.img_user_avatar);
            commentImage = itemView.findViewById(R.id.comment_image);
        }
    }
}