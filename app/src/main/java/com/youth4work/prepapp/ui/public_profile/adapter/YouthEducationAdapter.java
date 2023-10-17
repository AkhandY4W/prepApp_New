package com.youth4work.prepapp.ui.public_profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.response.user_profile.YouthEducationDetailsModel;
import com.youth4work.prepapp.util.CircleTransforms;

import java.util.ArrayList;

public class YouthEducationAdapter extends RecyclerView.Adapter<YouthEducationAdapter.ViewHolder> {
    Context mContext;
    YouthEducationDetailsModel mYouthEducationDetails;
    ArrayList<YouthEducationDetailsModel.SchoolEducationDetails> mSchoolEducationDetails;
    ArrayList<YouthEducationDetailsModel.CollegeEducationDetails> mCollegeEducationDetails;
    int size;

    public YouthEducationAdapter() {
    }

    public YouthEducationAdapter(Context context, YouthEducationDetailsModel youthEducationDetails) {
        this.mContext = context;
        this.mYouthEducationDetails = youthEducationDetails;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_education_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mYouthEducationDetails.getSchoolEducationDetails() != null || mYouthEducationDetails.getCollegeEducationDetails() != null) {
            mSchoolEducationDetails = mYouthEducationDetails.getSchoolEducationDetails();
            mCollegeEducationDetails = mYouthEducationDetails.getCollegeEducationDetails();
            if (mSchoolEducationDetails.size() != 0 || mCollegeEducationDetails.size() != 0) {
                if (position < mCollegeEducationDetails.size()) {
                    holder.txtCourseName.setText(mCollegeEducationDetails.get(position).getCourse());
                    holder.txtCollegeName.setText(mCollegeEducationDetails.get(position).getCollege());
                    holder.txtSession.setText(mCollegeEducationDetails.get(position).getBatchStart() + " - " + mCollegeEducationDetails.get(position).getBatchEnd());
                    Picasso.get().load(mCollegeEducationDetails.get(position).getCollegeLogo()).transform(new CircleTransforms()).into(holder.collegeImage);
                    if (mCollegeEducationDetails.get(position).getAchievement().equals("")) {
                        holder.txtAchievement.setVisibility(View.GONE);
                        holder.txtAchievementDummy.setVisibility(View.GONE);

                    } else {
                        holder.txtAchievement.setVisibility(View.VISIBLE);
                        holder.txtAchievementDummy.setVisibility(View.VISIBLE);
                        holder.txtAchievement.setText(mCollegeEducationDetails.get(position).getAchievement());

                    }

                } else if (position >= mCollegeEducationDetails.size() && position < mSchoolEducationDetails.size() + mCollegeEducationDetails.size()) {
                    holder.txtCourseName.setText(mSchoolEducationDetails.get(position - mCollegeEducationDetails.size()).getClassName() + "Th");
                    holder.txtCollegeName.setText(mSchoolEducationDetails.get(position - mCollegeEducationDetails.size()).getSchoolName());
                    holder.txtSession.setText(mSchoolEducationDetails.get(position - mCollegeEducationDetails.size()).getPassOut() + "");
                    holder.txtAchievement.setVisibility(View.GONE);
                    holder.txtAchievementDummy.setVisibility(View.GONE);
                }
            }
            if (position == (getItemCount() - 1)) {
                holder.view.setVisibility(View.GONE);

            } else {
                holder.view.setVisibility(View.VISIBLE);
            }

        }


    }

    @Override
    public int getItemCount() {
        size = mYouthEducationDetails.getCollegeEducationDetails().size() + mYouthEducationDetails.getSchoolEducationDetails().size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSession, txtCollegeName, txtCourseName, txtAchievement, txtAchievementDummy;
        ImageView collegeImage;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCollegeName = itemView.findViewById(R.id.txt_college_name);
            txtCourseName = itemView.findViewById(R.id.txt_course_name);
            txtAchievementDummy = itemView.findViewById(R.id.txt_achievement_dummy);
            txtAchievement = itemView.findViewById(R.id.txt_achievement);
            txtSession = itemView.findViewById(R.id.txt_session);
            collegeImage = itemView.findViewById(R.id.college_image);
            view = itemView.findViewById(R.id.view);
        }
    }
}
