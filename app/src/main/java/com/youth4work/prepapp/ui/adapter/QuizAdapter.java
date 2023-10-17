package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Question;

import java.util.List;


public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_OPTION = 1;

    private static OnItemClickListener listener;
    private boolean isCheckAnswer;
    private boolean isSelectionOption;
    private Question question;
    Context context;
    @NonNull
    private String optionsPrefix[] = {"A. ", "B. ", "C. ", "D. "};

    public QuizAdapter(Question question, boolean isSelectionOption, boolean checkAnswer, Context context) {
        setHasStableIds(true);
        this.question = question;
        this.isSelectionOption = isSelectionOption;
        this.isCheckAnswer = checkAnswer;
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        QuizAdapter.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_QUESTION)
            return new QuestionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_question, viewGroup, false));
        else
            return new OptionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_option, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder dailyTestViewHolder, int position) {

        if (dailyTestViewHolder.getItemViewType() == TYPE_QUESTION) {
            if (question.getQuestion().isEmpty()) {
                ((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setVisibility(View.GONE);
            } else {
                ((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setText(question.getQuestion());
               ((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setTextColor(Color.parseColor("#FFFFFF"));
                /*((QuestionViewHolder) dailyTestViewHolder).txtQuestion.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, String url) {
                        // No longer interested in upcoming events - unregister
                        view.setWebViewClient(null);
                        // Now data is loaded, can make RecyclerView visible
                    }
                });*/
               }
            if (!question.getQuestionImgUrl().isEmpty()) {
                (((QuestionViewHolder) dailyTestViewHolder).qsImage).setVisibility(View.VISIBLE);
                Picasso.get().load(question.getQuestionImgUrl()).into(((QuestionViewHolder) dailyTestViewHolder).qsImage);
            }
        } else if (dailyTestViewHolder.getItemViewType() == TYPE_OPTION) {
            List<Question.Options> options = question.getOptions();
            if (options.get(position - 1).getOption().isEmpty()) {
                ((OptionViewHolder) dailyTestViewHolder).txtOption.setVisibility(View.GONE);
            } else {
                ((OptionViewHolder) dailyTestViewHolder).txtOption.setText(Html.fromHtml("<strong>" + optionsPrefix[position - 1] + "</strong>     " + options.get(position - 1).getOption()));
              ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#000000"));
            }
            if (!options.get(position - 1).getOptionImgUrl().isEmpty()) {
                ((OptionViewHolder) dailyTestViewHolder).imgAnsImage.setVisibility(View.VISIBLE);
                Picasso.get().load(options.get(position - 1).getOptionImgUrl()).into(((OptionViewHolder) dailyTestViewHolder).imgAnsImage);
            }

            if (isSelectionOption) {
                if (options.get(position - 1).isSelected()) {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#FFC108"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#252525"));
                }
            } else if (isCheckAnswer) {
                if (options.get(position - 1).isAnswer()) {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#4BAE51"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#FFFFFF"));
                } else if (options.get(position - 1).isSelected()) {
                    ((OptionViewHolder) dailyTestViewHolder).lytOptionContainer.setBackgroundColor(Color.parseColor("#F44336"));
                    ((OptionViewHolder) dailyTestViewHolder).txtOption.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
    }

    public void updateList(Question question, boolean isSelectionOption, boolean checkAnswer) {
        this.question = question;
        this.isSelectionOption = isSelectionOption;
        this.isCheckAnswer = checkAnswer;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position > 0 ? TYPE_OPTION : TYPE_QUESTION;
    }

    @Override
    public int getItemCount() {
        return question.getOptions().size()+1;
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
        LinearLayout lytOptionContainer;
        QuestionViewHolder(@NonNull final View itemView) {
            super(itemView);

            lytOptionContainer = itemView.findViewById(R.id.question_container);
            txtQuestion = itemView.findViewById(R.id.txt_question);
            qsImage = itemView.findViewById(R.id.qsImage);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}
