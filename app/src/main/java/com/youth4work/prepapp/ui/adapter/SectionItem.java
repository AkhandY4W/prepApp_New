package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Section;

import java.util.List;

/**
 * Created by y4wmac on 17/06/16.
 */

public class SectionItem extends RecyclerView.Adapter<SectionItem.SectionItemHolder> {

    private  List<Section> sections;
    private static OnItemClickListener listener;
    public SectionItem(List<Section> sec)
    {
        sections=sec;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public int getItemCount() {return sections!=null?sections.size():0;}
    @Override
    public SectionItemHolder onCreateViewHolder(@NonNull ViewGroup vg,int position)
    {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.item_section_test, vg, false);
        SectionItemHolder pvh = new SectionItemHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(@NonNull SectionItemHolder sectionViewHolder, int i) {
        sectionViewHolder.txtCatName.setText(sections.get(i).getSubject());
        sectionViewHolder.txtAsp.setText(sections.get(i).getAspirants()>1000?sections.get(i).getAspirants()/1000+"k Aspirants":sections.get(i).getAspirants()+" Aspirants");
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public static class SectionItemHolder extends RecyclerView.ViewHolder
    {
        TextView txtTakeTest,txtAsp,txtCatName;
        SectionItemHolder(@NonNull View itemView)
        {
            super(itemView);
            txtTakeTest = itemView.findViewById(R.id.txt_take_test);
            txtAsp = itemView.findViewById(R.id.txt_asp);
            txtCatName = itemView.findViewById(R.id.txt_cat_name);
            itemView.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            });
        }
    }
}
