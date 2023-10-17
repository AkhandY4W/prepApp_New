package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.CouponCode;
import com.youth4work.prepapp.ui.payment.UpgradePlanActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Anil Sharma on 1/9/2018.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {

    private static CouponAdapter.OnItemClickListener listener;
    private List<CouponCode> couponCodes;
    Context context;

    public CouponAdapter(List<CouponCode> couponCodes, Context context) {
        this.couponCodes = couponCodes;
        this.context=context;
    }

    public void setOnItemClickListener(CouponAdapter.OnItemClickListener listener) {
        CouponAdapter.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public CouponAdapter.CouponViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.couponlayout, viewGroup, false);
        CouponAdapter.CouponViewHolder pvh = new CouponAdapter.CouponViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.CouponViewHolder personViewHolder, int i) {
        personViewHolder.couponCode.setText(couponCodes.get(i).getCouponCode());
        personViewHolder.couponcodedesc.setText(couponCodes.get(i).getCouponCodeDesc());
        personViewHolder.btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "clicked position:" + couponCodes.get(i).getCouponCode());
                Intent intent=new Intent(context,UpgradePlanActivity.class);
                intent.putExtra("promocode",couponCodes.get(i).getCouponCode());
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                AppEventsLogger appEventsLogger=AppEventsLogger.newLogger(context);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,couponCodes.get(i).getCouponCode());
                bundle.putString(FirebaseAnalytics.Param.START_DATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO, bundle);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponCodes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class CouponViewHolder extends RecyclerView.ViewHolder {

        TextView couponCode;
        TextView couponcodedesc;
        Button btnapply;

        CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            couponCode = (TextView) itemView.findViewById(R.id.txtcouponcode);
            couponcodedesc = (TextView) itemView.findViewById(R.id.txtcoupondiscription);
            btnapply = (Button) itemView.findViewById(R.id.btnapplycoupon);
            itemView.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            });

        }
    }
}
