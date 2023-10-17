package com.youth4work.prepapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.skyhope.showmoretextview.ShowMoreTextView
import com.squareup.picasso.Picasso
import com.youth4work.prepapp.R
import com.youth4work.prepapp.network.model.Category
import kotlinx.android.synthetic.main.single_sub_category_item.view.*

class SubCategoryListingAdapter(
    val mContext: Context,
    val category: List<Category>,
    val className: String
) : RecyclerView.Adapter<SubCategoryListingAdapter.ViewHolder>(), Filterable {
    var categoryFilterList = ArrayList<Category>()
    lateinit var mListener: OnItemClickListener

    init {
        categoryFilterList=category as ArrayList<Category>
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(
            context: Context,
            category: Category,
            className: String,
            mListener: OnItemClickListener
        ) {
            val imgCat = itemView.findViewById<ImageView>(R.id.img_cat)
            val txtCat = itemView.findViewById<TextView>(R.id.txt_cat)
            val txtAboutExam = itemView.findViewById<ShowMoreTextView>(R.id.txt_about_exam)
            val txtAsp = itemView.findViewById<TextView>(R.id.txt_asp)
            val forumLayout = itemView.findViewById<LinearLayout>(R.id.forum_layout)
            val btnTakeTest = itemView.findViewById<Button>(R.id.btn_take_test)
            txtCat.setText(category.category)
           if (className == "CategoryExamsActivity") {
                txtAboutExam.setText(category.subDescription)
                txtAsp.setText(category.aspirants)
                if (category.subCategoryImg != "" && category.subCategoryImg != null) {
                    Picasso.get().load(category.subCategoryImg).into(imgCat)
                }
            } else {
                txtAboutExam.setText(category.subCatDescription)
                txtAsp.setText(category.attempts.toString())
                if (category.subCatImg != "" && category.subCatImg != null) {
                    Picasso.get().load(category.subCatImg).into(imgCat)
                }
            }
            txtAboutExam.setShowingLine(3)
            txtAboutExam.addShowMoreText("READ MORE")
            //txtAboutExam.addShowLessText("READ LESS")
            txtAboutExam.setShowMoreColor(context.resources.getColor(R.color.text_grey))
            txtAboutExam.setShowLessTextColor(context.resources.getColor(R.color.text_grey))

            btnTakeTest.setOnClickListener {
                if (mListener != null) {
                    mListener.onTakeTestClick(category)
                }
            }
            forumLayout.setOnClickListener {
                if (mListener != null) {
                    mListener.onForumClick(category)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_sub_category_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mContext, category.get(position), className, mListener)

    }

    override fun getItemCount(): Int {
        return category.size
    }

    interface OnItemClickListener {
        fun onForumClick(category: Category) {

        }

        fun onTakeTestClick(category: Category) {

        }
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    categoryFilterList = category as ArrayList<Category>
                } else {
                    val resultList = ArrayList<Category>()
                    for (row in category) {
                        if (row.category.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    categoryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = categoryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoryFilterList = results?.values as ArrayList<Category>
                notifyDataSetChanged()
            }
        }
    }

}