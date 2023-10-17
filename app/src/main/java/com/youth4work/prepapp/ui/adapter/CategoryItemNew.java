package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.ui.home.CategoryExamsActivity;
import com.youth4work.prepapp.util.CenterZoomLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemNew extends RecyclerView.Adapter<CategoryItemNew.ItemRowHolder> implements Filterable {
    List<ParentCategory> mCategories;
    List<ParentCategory> mCategoriesFiltered;
    String className;
    Context self;

    public CategoryItemNew(List<ParentCategory> mCategories, String className, Context self) {
        this.className=className;
        this.mCategories=mCategories;
        this.mCategoriesFiltered=mCategories;
        this.self=self;
    }

    @NonNull
    @Override
    public CategoryItemNew.ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_layout, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemNew.ItemRowHolder holder, int position) {
        holder.itemTitle.setText(mCategoriesFiltered.get(position).getCategory());
        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(self, mCategoriesFiltered.get(position).getSubCatlist(),mCategoriesFiltered.get(position).getCategory());
        holder.recycler_view_list.setHasFixedSize(true);
        CenterZoomLayoutManager layoutManager =
                new CenterZoomLayoutManager(self, LinearLayoutManager.HORIZONTAL, false);
        holder.recycler_view_list.setLayoutManager(layoutManager);
        //new LinearSnapHelper().attachToRecyclerView(holder.recycler_view_list);
        holder.recycler_view_list.setAdapter(itemListDataAdapter);

        if(mCategoriesFiltered.get(position).getCategory().equals("Popular")||mCategoriesFiltered.get(position).getSubCatlist().size()<5){
            holder.btnMore.setVisibility(View.GONE);
        }
        else {
            holder.btnMore.setVisibility(View.VISIBLE);
            holder.btnMore.setOnClickListener(v -> {
                Category categorynew = new Category(mCategoriesFiltered.get(position).getCatid());
                categorynew.setCatid(mCategoriesFiltered.get(position).getCatid());
                categorynew.setAttempts(mCategoriesFiltered.get(position).getCatAttempts());
                categorynew.setCategory(mCategoriesFiltered.get(position).getCategory());
                CategoryExamsActivity.show(self,categorynew);
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return mCategoriesFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        RecyclerView recycler_view_list;
        TextView btnMore;

         ItemRowHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            recycler_view_list = itemView.findViewById(R.id.recycler_view_list);
            btnMore= itemView.findViewById(R.id.btnMore);

        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = mCategories.size();
                    filterResults.values = mCategories;

                }else{
                    List<ParentCategory> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(ParentCategory itemsModel:mCategories){
                        if(itemsModel.getCategory().toLowerCase().contains(searchStr) || check(searchStr,itemsModel.getSubCatlist())){
                                resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mCategoriesFiltered = (List<ParentCategory>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    private boolean check(String searchText,List<ParentCategory.SubCat> list ){
        boolean found=false;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getSubCategory().toLowerCase().contains(searchText.toLowerCase())){
                found=true;
            }
        }
        return found;
    }
}

