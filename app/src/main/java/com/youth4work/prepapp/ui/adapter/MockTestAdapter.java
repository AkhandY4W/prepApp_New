package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.AllMockQSModel;

import java.util.ArrayList;
import java.util.List;

public class MockTestAdapter extends ExpandableRecyclerAdapter<MockTestAdapter.QuestionListItem> {
    public static final int TYPE_PERSON = 1001;
    public static final int TYPE_HEADER = 1000;
    Context self;
    List<QuestionListItem> allMockQSModels;
    int subCatId;
    ArrayList<AllMockQSModel.MockQuestion> mockQuestionArrayList;
    private static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MockTestAdapter(Context context, List<QuestionListItem> allMockQSModelsList, ArrayList<AllMockQSModel.MockQuestion> mockQuestionArrayList) {
        super(context);
        this.self = context;
        this.allMockQSModels = allMockQSModelsList;
        this.mockQuestionArrayList = mockQuestionArrayList;
        setItems(allMockQSModelsList);

    }

    public static class QuestionListItem extends ExpandableRecyclerAdapter.ListItem {
        public String txtQuestionNo, txtQuestion;
        public String txtSubCatId;

        public QuestionListItem(String group, int subcatid) {
            super(TYPE_HEADER);
            txtQuestionNo = group;
            txtSubCatId = String.valueOf(subcatid);
        }

        public QuestionListItem(int questionno, String question) {
            super(TYPE_PERSON);
            txtQuestion = question;
            txtQuestionNo = String.valueOf(questionno);
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView txtTestName;
        ImageView imageView;

        public HeaderViewHolder(View view) {
            super(view, view.findViewById(R.id.image));
            txtTestName = view.findViewById(R.id.txt_test_name);
            imageView = view.findViewById(R.id.image);

        }




    }

    public class PersonViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView txtQuestionNo, txtQuestion;
        ImageView imageView, imageView2;
        ProgressRelativeLayout progressActivity;

        public PersonViewHolder(View view) {
            super(view);
            txtQuestionNo = view.findViewById(R.id.txt_question_no);
            txtQuestion = view.findViewById(R.id.txt_question);
            imageView = view.findViewById(R.id.image_view);
            progressActivity = view.findViewById(R.id.progressActivity);
            imageView2 = view.findViewById(R.id.image_mark_for_review);
        }


    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {

            return new HeaderViewHolder(inflate(R.layout.test_header_layout, parent));
        }
        else
        {
            return new PersonViewHolder(inflate(R.layout.question_layout, parent));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {

            ((HeaderViewHolder)holder).txtTestName.setText(visibleItems.get(position).txtQuestionNo);
            subCatId = Integer.parseInt(visibleItems.get(position).txtSubCatId);
        }
        else
        {
            int sno=Integer.parseInt(visibleItems.get(position).txtQuestionNo);
            if (sno-1 < mockQuestionArrayList.size()) {
                if (mockQuestionArrayList.get(sno-1).isIsattempted()) {
                    ((PersonViewHolder)holder).imageView.setImageResource(R.drawable.ic_attempted_24dp);
                    ((PersonViewHolder)holder).imageView2.setVisibility(View.GONE);
                    ((PersonViewHolder)holder).txtQuestionNo.setTextColor(Color.parseColor("#FFFFFF"));
                } else if (mockQuestionArrayList.get(sno-1).isIsflagged()) {
                    ((PersonViewHolder)holder).imageView.setImageResource(R.drawable.ic_unattempted_24dp);
                    ((PersonViewHolder)holder).imageView2.setVisibility(View.VISIBLE);
                    ((PersonViewHolder)holder).txtQuestionNo.setTextColor(Color.parseColor("#666666"));
                } else {
                    ((PersonViewHolder)holder).imageView.setImageResource(R.drawable.ic_unattempted_24dp);
                    ((PersonViewHolder)holder).imageView2.setVisibility(View.GONE);
                    ((PersonViewHolder)holder).txtQuestionNo.setTextColor(Color.parseColor("#666666"));
                }
            }
            ((PersonViewHolder)holder).txtQuestionNo.setText(visibleItems.get(position).txtQuestionNo);
            ((PersonViewHolder)holder).txtQuestion.setText(Html.fromHtml(visibleItems.get(position).txtQuestion));
            ((PersonViewHolder)holder).txtQuestion.setOnClickListener(v -> {
                // Triggers click upwards to the adapter on click
                if (listener != null)

                    listener.onItemClick(((PersonViewHolder)holder).txtQuestion, sno);
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
