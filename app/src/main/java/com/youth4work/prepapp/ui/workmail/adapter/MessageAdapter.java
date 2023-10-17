package com.youth4work.prepapp.ui.workmail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.response.Message;
import com.youth4work.prepapp.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by  Samar on 9/6/2016.
 *
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder>
{
    private List<Message> lstMessages;
    private ArrayList<Message> arraylist;
    private OnItemClickListner listener;
    Context context;
    public MessageAdapter(List<Message> frm, Context context)
    {
        lstMessages=frm;
        this.context= context;
        this.arraylist = new ArrayList<Message>();
        this.arraylist.addAll(frm);
    }
    public void setOnItemClickListner(OnItemClickListner ln)
    {
        listener=ln;
    }
    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup vg, int postion)
    {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.notifications, vg, false);
        MessagesViewHolder pvh = new MessagesViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(MessagesViewHolder vh,int postion)
    {
        if(!lstMessages.get(postion).isRead()){
            vh.title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            vh.time.setTextColor(context.getResources().getColor(R.color.txt_black_70));
        }
        else {
            vh.time.setTextColor(context.getResources().getColor(R.color.txt_black_30));
            vh.title.setTextColor(context.getResources().getColor(R.color.txt_black_70));
        }
        String title = lstMessages.get(postion).getSenderName();
        if(title!=null)
            vh.title.setText(title);
        String talent = lstMessages.get(postion).getMessageBody();
        if(talent!=null)
            vh.talent.setText(talent);
        String posted=lstMessages.get(postion).getSentDate();
        if(posted!=null)
            vh.time.setText(Constants.getDateMonth(posted));
        if(!"null".equals(lstMessages.get(postion).getSenderUserPic()))
        Picasso.get().load(lstMessages.get(postion).getSenderUserPic()).placeholder(R.drawable.ic_user_default).into(vh.profile_pic);
    }
    @Override
    public int getItemCount()
    {
        return lstMessages.size();
    }
    public interface OnItemClickListner
    {
        void onItemClick(View itemView, int position);
    }
    public class MessagesViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView talent;
        ImageView profile_pic;
        TextView time;
        public MessagesViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            talent= view.findViewById(R.id.company);
            time= view.findViewById(R.id.location);
            profile_pic= view.findViewById(R.id.imageView);
            view.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(view, getLayoutPosition());
            });
        }


    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lstMessages.clear();
        if (charText.length() == 0) {
            lstMessages.addAll(arraylist);
        } else {
            for (Message wp : arraylist) {
                if (wp.getSenderName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lstMessages.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
