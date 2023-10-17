package com.youth4work.prepapp.ui.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Category;

public class ParentCategoryItem extends AbstractItem<ParentCategoryItem, ParentCategoryItem.ViewHolder> {
    public Category category;
    private String colorHex[] = {"#55bb92", "#f5ab4f", "#563d81", "#278ce0"};

    public ParentCategoryItem(Category category) {
        this.category = category;
    }

    @Override
    public int getType() {
        return R.id.parent_category_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_parent_category;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);
        viewHolder.categoryContainer.setBackgroundColor(Color.parseColor(colorHex[viewHolder.getAdapterPosition() % 4]));
        viewHolder.categoryName.setText(category.getCategory());
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RelativeLayout categoryContainer;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.txt_category_name);
            categoryContainer = itemView.findViewById(R.id.category_container);
        }
    }
}