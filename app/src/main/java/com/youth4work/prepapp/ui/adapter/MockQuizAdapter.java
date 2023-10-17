package com.youth4work.prepapp.ui.adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.AllMockQSModel;
import com.youth4work.prepapp.network.model.Question;
import com.youth4work.prepapp.ui.views.TouchImageView;
import com.youth4work.prepapp.util.Constants;

import java.util.List;


public class MockQuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_OPTION = 1;

    private static OnItemClickListener listener;
    private boolean isCheckAnswer;
    private boolean isSelectionOption;
    private AllMockQSModel.MockQuestion mockQuestion;
    Context context;
    //ArrayList<AllMockQSModel.MockQuestion> mockQuestionArrayList;

    @NonNull
    private String optionsPrefix[] = {"A. ", "B. ", "C. ", "D. "};


    public MockQuizAdapter(AllMockQSModel.MockQuestion question, boolean isSelectionOption, boolean checkAnswer, Context context) {
        this.mockQuestion = question;
        this.isSelectionOption = isSelectionOption;
        this.isCheckAnswer = checkAnswer;
        this.context = context;
        //this.mockQuestionArrayList=mockQuestionArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        MockQuizAdapter.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_QUESTION)
            return new QuestionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mock_question, viewGroup, false));
        else
            return new OptionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mock_option_prep, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder dailyTestViewHolder, int position) {

        if (dailyTestViewHolder.getItemViewType() == TYPE_QUESTION) {

            if (mockQuestion.getQuestion().isEmpty()) {
                ((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setVisibility(View.GONE);
            } else {
                ((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setText(Html.fromHtml(mockQuestion.getQuestion()));
            }
            if (!mockQuestion.getQuestionImageUrl().isEmpty()) {
                (((QuestionViewHolder) dailyTestViewHolder).qsImage).setVisibility(View.VISIBLE);
                Picasso.get().load(mockQuestion.getQuestionImageUrl()).into(((QuestionViewHolder) dailyTestViewHolder).qsImage);
            }
            ((QuestionViewHolder) dailyTestViewHolder).qsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.ShowPopup(mockQuestion.getQuestionImageUrl(),context);
                }
            });
        } else if (dailyTestViewHolder.getItemViewType() == TYPE_OPTION) {
            List<Question.Options> options = mockQuestion.getOptions();
            if (options.get(position - 1).getOption().isEmpty()) {
                ((OptionViewHolder) dailyTestViewHolder).txtOption.setVisibility(View.GONE);
            } else {
                //ArrayList<AllMockQSModel.MockQuestion> mockQuestionArrayList = new PreferencesManager(context).getMockQuestions(context);
                if (mockQuestion.isIsattempted()) {
                    int selectedOptionID = mockQuestion.getOptionChoosen();
                    if (Integer.parseInt(options.get(position - 1).getOptionID()) == selectedOptionID) {
                        ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#087ccd"));
                        ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#252525"));
                    }
                }

                ((OptionViewHolder) dailyTestViewHolder).txtOption.setText(Html.fromHtml(("<strong>" + optionsPrefix[position - 1] + "</strong>     " + context.getString(R.string.tab) + options.get(position - 1).getOption())));
            }

            if (!options.get(position - 1).getOptionImgUrl().isEmpty()) {
                ((OptionViewHolder) dailyTestViewHolder).imgAnsImage.setVisibility(View.VISIBLE);
                Picasso.get().load(options.get(position - 1).getOptionImgUrl()).into(((OptionViewHolder) dailyTestViewHolder).imgAnsImage);
            }

            if (isSelectionOption) {
                if (options.get(position - 1).isSelected()) {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#087ccd"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#252525"));
                }
            }
        }
    }

    public void updateList(AllMockQSModel.MockQuestion question, boolean isSelectionOption) {
        this.mockQuestion = question;
        this.isSelectionOption = isSelectionOption;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position > 0 ? TYPE_OPTION : TYPE_QUESTION;
    }

    @Override
    public int getItemCount() {
        return mockQuestion.getOptions().size()+1;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public static class OptionViewHolder extends RecyclerView.ViewHolder {

        TextView txtOption;
        ImageView imgAnsImage;
        LinearLayout lytOptionContainer;

        OptionViewHolder(@NonNull final View itemView) {
            super(itemView);

            lytOptionContainer = itemView.findViewById(R.id.option_container);
            txtOption = itemView.findViewById(R.id.txt_option);
            imgAnsImage = itemView.findViewById(R.id.ansImage);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(itemView, getLayoutPosition());

            });
        }
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion;
        ImageView qsImage;
        RelativeLayout lytQuestionContainer;
        QuestionViewHolder(@NonNull final View itemView) {
            super(itemView);

            lytQuestionContainer = itemView.findViewById(R.id.question_container);
            txtQuestion = itemView.findViewById(R.id.txt_question);
            qsImage = itemView.findViewById(R.id.qsImage);
        }
    }

}
