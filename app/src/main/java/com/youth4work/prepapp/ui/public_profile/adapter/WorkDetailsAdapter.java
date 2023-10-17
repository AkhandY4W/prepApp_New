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
import com.youth4work.prepapp.network.model.response.user_profile.YouthWorkDetailsModel;
import com.youth4work.prepapp.util.CircleTransforms;

import java.util.ArrayList;



public class WorkDetailsAdapter extends RecyclerView.Adapter<WorkDetailsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<YouthWorkDetailsModel.WorkDetails> workDetails;
    public String totalDuration;

    public WorkDetailsAdapter() {

    }

    public WorkDetailsAdapter(Context context, ArrayList<YouthWorkDetailsModel.WorkDetails> workDetailsModels) {
        this.mContext = context;
        this.workDetails = workDetailsModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_work_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtJobName.setText(workDetails.get(position).getJobTitle());
        holder.txtCompanyName.setText(workDetails.get(position).getCompany());
        if (workDetails.get(position).getIsPresent()) {
            holder.txtTimePeriod.setText(workDetails.get(position).getWorkFrom() + " - " + "Present" + getTotalDuration(workDetails.get(position).getTotalMonth()));

        } else {
            holder.txtTimePeriod.setText(workDetails.get(position).getWorkFrom() + " - " + workDetails.get(position).getWorkTo() + getTotalDuration(workDetails.get(position).getTotalMonth()));
        }
        Picasso.get().load(workDetails.get(position).getLogo()).transform(new CircleTransforms()).into(holder.companyLogo);
        if (workDetails.get(position).getResponsibility().equals("")) {
            holder.txtkeyResponsibilities.setVisibility(View.GONE);
            holder.TxtkeyResponsibilitiesDummy.setVisibility(View.GONE);
        } else {
            holder.txtkeyResponsibilities.setVisibility(View.VISIBLE);
            holder.TxtkeyResponsibilitiesDummy.setVisibility(View.VISIBLE);
            holder.txtkeyResponsibilities.setText(workDetails.get(position).getResponsibility());

        }if (workDetails.get(position).getAchievement().equals("")) {
            holder.txtAchievementDummy.setVisibility(View.GONE);
            holder.txtAchievement.setVisibility(View.GONE);
        } else {
            holder.txtAchievementDummy.setVisibility(View.VISIBLE);
            holder.txtAchievement.setVisibility(View.VISIBLE);
            holder.txtAchievement.setText(workDetails.get(position).getAchievement());

        }
        if (position == (getItemCount() - 1)) {
            holder.view.setVisibility(View.GONE);

        } else {
            holder.view.setVisibility(View.VISIBLE);
        }

    }

    private String getTotalDuration(int totalMonth) {
        int year = totalMonth / 12;
        int month = totalMonth % 12;
        totalDuration = "(" + year + " year " + month + " month)";
        return totalDuration;
    }


    @Override
    public int getItemCount() {
        return workDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView companyLogo;
        TextView txtJobName, txtCompanyName,
                txtTimePeriod, txtkeyResponsibilities,
                TxtkeyResponsibilitiesDummy, txtAchievement,
                txtAchievementDummy;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.company_logo);
            txtJobName = itemView.findViewById(R.id.job_name);
            txtCompanyName = itemView.findViewById(R.id.company_name);
            txtTimePeriod = itemView.findViewById(R.id.time_period);
            txtkeyResponsibilities = itemView.findViewById(R.id.key_responsibilities);
            TxtkeyResponsibilitiesDummy = itemView.findViewById(R.id.key_responsibilities_dummy);
            txtAchievement = itemView.findViewById(R.id.key_achievement);
            txtAchievementDummy = itemView.findViewById(R.id.key_achievement_dummy);
            view = itemView.findViewById(R.id.view);
        }
    }
}
