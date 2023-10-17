package com.youth4work.prepapp.ui.adapter;

import android.graphics.Paint;
import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Plan;

import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlanViewHolder> {

    private static OnItemClickListener listener;
    private List<Plan> plans;

    public PlansAdapter(List<Plan> plans) {
        this.plans = plans;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        PlansAdapter.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plan, viewGroup, false);
        PlanViewHolder pvh = new PlanViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder personViewHolder, int i) {
        personViewHolder.planName.setText(plans.get(i).getServiceName());
        personViewHolder.planDescription.setText(plans.get(i).getServiceDesc());
        personViewHolder.planPrice.setText("{fa-inr} " + plans.get(i).getDisAmount() + " only");
        personViewHolder.realPrice.setText("{fa-inr} " +plans.get(i).getAmount());
        personViewHolder.realPrice.setPaintFlags(personViewHolder.realPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        if (plans.get(i).getDisAmount() == plans.get(i).getAmount())
        {
        personViewHolder.realPrice.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView planName;
        TextView planDescription;
        TextView planPrice;
        TextView realPrice;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            planName = itemView.findViewById(R.id.txt_plan_name);
            planDescription = itemView.findViewById(R.id.txt_plan_description);
            planPrice = (IconTextView) itemView.findViewById(R.id.txt_plan_price);
            realPrice=(IconTextView) itemView.findViewById(R.id.txt_real_price);
            itemView.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            });
        }
    }
}
