package com.youth4work.prepapp.ui.public_profile.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.response.user_profile.YouthWorkDetailsModel;

import java.util.ArrayList;


public class YouthProjectAdapter extends RecyclerView.Adapter<YouthProjectAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<YouthWorkDetailsModel.YouthProjects> mYouthProjects;

    public YouthProjectAdapter() {

    }

    public YouthProjectAdapter(Context context, ArrayList<YouthWorkDetailsModel.YouthProjects> youthProjects) {
        this.mContext = context;
        this.mYouthProjects = youthProjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_project_youth, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtProjectName.setText(mYouthProjects.get(position).getName());
        holder.txtDurationOfProject.setText(mYouthProjects.get(position).getFrom() + " - " + mYouthProjects.get(position).getTo());
        if (mYouthProjects.get(position).getURL().equals("")) {
            holder.txtProjectLink.setVisibility(View.GONE);
        } else {
            holder.txtProjectLink.setVisibility(View.VISIBLE);
            holder.txtProjectLink.setText(mYouthProjects.get(position).getURL());
            Linkify.addLinks(holder.txtProjectLink, Linkify.ALL);

        }
        holder.txtProjectDetail.setText(mYouthProjects.get(position).getDescription());
        if (position == (getItemCount() - 1)) {
             holder.view.setVisibility(View.GONE);

        } else {

            holder.view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mYouthProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProjectName, txtDurationOfProject,
                txtProjectLink, txtProjectDetail;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            txtProjectName = itemView.findViewById(R.id.project_name);
            txtDurationOfProject = itemView.findViewById(R.id.duration_of_project);
            txtProjectLink = itemView.findViewById(R.id.project_link);
            txtProjectDetail = itemView.findViewById(R.id.project_detail);
            view = itemView.findViewById(R.id.view);
        }
    }
}
