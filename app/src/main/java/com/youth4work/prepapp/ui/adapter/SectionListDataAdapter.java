package com.youth4work.prepapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.ui.home.DashboardActivity;

import java.util.List;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<ParentCategory.SubCat> itemsList;
    private Context mContext;
    private String parentCat;

    SectionListDataAdapter(Context context, List<ParentCategory.SubCat> itemsList, String parentCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.parentCat = parentCat;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_category, null);
        // This code is used to get the screen dimensions of the user's device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        // int height = displayMetrics.heightPixels;
        // Set the ViewHolder width to be a third of the screen size, and height to wrap content
        v.setLayoutParams(new RecyclerView.LayoutParams((int) (width / 2.50), RecyclerView.LayoutParams.WRAP_CONTENT));
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        if (parentCat.equals("Popular")) {
            holder.txtparentCat.setVisibility(View.VISIBLE);
            holder.txtparentCat.setText(itemsList.get(i).getCategory());
        }
        else {
            holder.txtparentCat.setVisibility(View.GONE);
        }
        if (!itemsList.get(i).getSubCategoryImages().equals("") && itemsList.get(i).getSubCategoryImages() != null)
           Picasso.get().load(itemsList.get(i).getSubCategoryImages()).into(holder.imgCat);
        holder.txtAsp.setText(itemsList.get(i).getSubCategoryAspirants()>1000? itemsList.get(i).getSubCategoryAspirants()/1000 + "k Aspirants":itemsList.get(i).getSubCategoryAspirants()+ " Aspirants");
        holder.txtCat.setText(itemsList.get(i).getSubCategory());
        holder.cardCat.setOnClickListener(v ->
                DashboardActivity.show(mContext, true, itemsList.get(i), parentCat));
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {
        CardView cardCat;
        ImageView imgCat;
        TextView txtparentCat, txtCat, txtAsp;

        public SingleItemRowHolder(View view) {

            super(view);
            cardCat = itemView.findViewById(R.id.card_cat);
            imgCat = itemView.findViewById(R.id.img_cat);
            txtparentCat = itemView.findViewById(R.id.txt_parent_cat);
            txtAsp = itemView.findViewById(R.id.txt_asp);
            txtCat = itemView.findViewById(R.id.txt_cat);


        }

    }

}
