package com.youth4work.prepapp.ui.adapter;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Section;

import java.util.List;

/**
 * Created by y4wmac on 17/06/16.
 */

public class MockSectionItem extends RecyclerView.Adapter<MockSectionItem.SectionItemHolder> {

    private List<Section> sections;
    private static OnItemClickListener listener;
    boolean isUserUpgarded;

    public MockSectionItem(List<Section> sec, boolean userUpgarded) {
        sections = sec;
        isUserUpgarded = userUpgarded;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return sections != null ? sections.size() : 0;
    }

    @Override
    public SectionItemHolder onCreateViewHolder(@NonNull ViewGroup vg, int position) {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.item_section_test, vg, false);
        SectionItemHolder pvh = new SectionItemHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull SectionItemHolder sectionViewHolder, int i) {
        if (i == 0 || isUserUpgarded){
            sectionViewHolder.txtTakeTest.setVisibility(View.VISIBLE);
            sectionViewHolder.imageView.setVisibility(View.GONE);
        } else {
            sectionViewHolder.imageView.setVisibility(View.VISIBLE);
            sectionViewHolder.txtTakeTest.setVisibility(View.GONE);
        }
        sectionViewHolder.subjectName.setText(sections.get(i).getSubject());
        //mUserManager.getCategory().getAttempts()>1000?mUserManager.getCategory().getAttempts()/1000+"k Aspriants":mUserManager.getCategory().getAttempts()+" Aspriants"
        sectionViewHolder.txtAsp.setText(sections.get(i).getAspirants()>1000?+sections.get(i).getAspirants()/1000+"k Aspriants":sections.get(i).getAspirants()+" Aspriants");
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    static class SectionItemHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subjectName,txtAsp,txtTakeTest;
        ImageView imageView;


        SectionItemHolder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            subjectName = itemView.findViewById(R.id.txt_cat_name);
            txtAsp = itemView.findViewById(R.id.txt_asp);
            txtTakeTest = itemView.findViewById(R.id.txt_take_test);
            imageView = itemView.findViewById(R.id.lock_image_view);
            itemView.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());
            });
        }
    }
}
