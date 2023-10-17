package com.youth4work.prepapp.ui.workmail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.network.model.response.Message;
import com.youth4work.prepapp.util.Constants;

import java.util.ArrayList;


public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private ArrayList<Message> mArrListMessages;
    private Context mContext;
    private String mUserId = "0";
    private User mUser;
    String msgDate = "";

    public ConversationAdapter(Context context, ArrayList<Message> arrListMessages) {
        this.mArrListMessages = arrListMessages;
        this.mContext = context;
        mUser = UserManager.getInstance(context).getUser();
        if (mUser != null && mUser.isLoggedIn())
            mUserId = String.valueOf(mUser.getUserId());
    }

    public void setArrListMessages(ArrayList<Message> arrListMessages) {
        this.mArrListMessages = arrListMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_conversation_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message msg = mArrListMessages.get(position);
        if (position == 0) {
            msgDate = Constants.getDateMonthTime(mArrListMessages.get(position).getSentDate()).trim();
            holder.mlayoutTime.setVisibility(View.VISIBLE);
        } else {
            msgDate = Constants.getDateMonthTime(mArrListMessages.get(position-1).getSentDate()).trim();
            if (msgDate.equals(Constants.getDateMonthTime(mArrListMessages.get(position).getSentDate()).trim())) {
                msgDate = Constants.getDateMonthTime(mArrListMessages.get(position).getSentDate()).trim();
                holder.mlayoutTime.setVisibility(View.GONE);
            } else {
                msgDate = Constants.getDateMonthTime(mArrListMessages.get(position).getSentDate()).trim();
                holder.mlayoutTime.setVisibility(View.VISIBLE);
            }
        }
        if ("0".equalsIgnoreCase(mUserId) || mUserId.equalsIgnoreCase(msg.getSenderUserId())) {
            holder.msgCardView.setBackgroundResource(R.drawable.conversation_right);
            holder.msgCardView.setVisibility(View.VISIBLE);
            holder.msgCardView2.setVisibility(View.GONE);
            if (position == mArrListMessages.size()) {
                holder.userImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.userImage.setVisibility(View.GONE);
            }
        } else {
            holder.msgCardView2.setBackgroundResource(R.drawable.conversation_left);
            holder.msgCardView.setVisibility(View.GONE);
            holder.msgCardView2.setVisibility(View.VISIBLE);
            holder.userImage.setVisibility(View.GONE);
        }
        Picasso.get().load(mUser.getImgUrl()).placeholder(R.drawable.ic_user_default).into(holder.userImage);
        holder.txtTime.setText(Constants.getDateMonthTime(msg.getSentDate()));
        holder.txtUserMsg.setText(Html.fromHtml(msg.getMessageBody()));
        holder.txtUserMsg2.setText(Html.fromHtml(msg.getMessageBody()));
        Linkify.addLinks(holder.txtUserMsg, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
        Linkify.addLinks(holder.txtUserMsg2, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS);
        holder.txtUserMsg.setLinkTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public int getItemCount() {
        return mArrListMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout msgCardView, msgCardView2;
        LinearLayout mlayoutTime;
        TextView txtUserMsg, txtUserMsg2, txtTime;
        ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mlayoutTime = itemView.findViewById(R.id.layout_time);
            msgCardView = itemView.findViewById(R.id.msg_card_view);
            msgCardView2 = itemView.findViewById(R.id.msg_card_view2);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtUserMsg = itemView.findViewById(R.id.txt_user_msg);
            txtUserMsg2 = itemView.findViewById(R.id.txt_sender_msg);
            userImage = itemView.findViewById(R.id.user_img);
        }
    }
}
