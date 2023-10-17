package com.youth4work.prepapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.youth4work.prepapp.databinding.SingleUserItemBinding
import com.youth4work.prepapp.network.model.response.Message
import com.youth4work.prepapp.network.model.response.Youth
import com.youth4work.prepapp.network.model.response.YouthListResponse
import com.youth4work.prepapp.ui.public_profile.ProfileActivity
import com.youth4work.prepapp.ui.workmail.ConversationActivity


class ChatUserListingAdapter(val mContext:Context,val youthList:ArrayList<Youth>): RecyclerView.Adapter<ChatUserListingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val custom=SingleUserItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val layout=SingleUserItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(layout.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.custom.txtUserFullName.text=youthList.get(position).name
        Picasso.get().load(youthList.get(position).imageURL).into(holder.custom.imgUser)
        holder.custom.txtChatNow.setOnClickListener {
            var intent = Intent(mContext, ConversationActivity::class.java)
            val msg = Message()
            msg.setSenderUserId(youthList.get(position).userId.toString())
            val gson = Gson()
            val m: String = gson.toJson(msg, Message::class.java)
            intent.putExtra("msg", m)
            mContext.startActivity(intent)
        }
        holder.custom.cardUser.setOnClickListener {
            ProfileActivity.show(mContext,youthList.get(position).name,youthList.get(position).imageURL,youthList.get(position).userId)
        }
    }

    override fun getItemCount(): Int {
        return youthList.size
    }
}