package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Attempt;
import com.youth4work.prepapp.network.model.Question;
import com.youth4work.prepapp.ui.quiz.DiscussionActivity;

import java.util.ArrayList;


public class AttemptItem extends AbstractItem<AttemptItem, AttemptItem.ViewHolder> {
    public Attempt attempt;
    public Context context;

    public AttemptItem(Context context,Attempt attempt) {
        this.attempt = attempt;
        this.context=context;
    }

    @Override
    public int getType() {
        return R.id.attempt_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_attempt;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        viewHolder.txtQuestion.setText(Html.fromHtml(attempt.getQuestion()));
       /* viewHolder.txtQuestion.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // No longer interested in upcoming events - unregister
                view.setWebViewClient(null);
                // Now data is loaded, can make RecyclerView visible
            }
        });*/
        if(attempt.isWinOrLost()){
            viewHolder.txtAnswerDumy2.setVisibility(View.GONE);
            viewHolder.txtAnswerRight.setVisibility(View.GONE);
            //viewHolder.txtAnswerDumy.setText("Your ans.");
            viewHolder.txtAnswerDumy.setTextColor(context.getResources().getColor(R.color.fruit_salad));
            viewHolder.txtUserSelectedAnswer.setText(attempt.getUserAnswer());
            if(attempt.getUserselectedImageUrl()!=null&&!attempt.getUserselectedImageUrl().equals("")){
                viewHolder.userSelectedImage.setVisibility(View.VISIBLE);
                Picasso.get().load(attempt.getUserselectedImageUrl()).into(viewHolder.userSelectedImage);
            }
        }
        else{
            viewHolder.txtAnswerDumy.setVisibility(View.VISIBLE);
            viewHolder.txtUserSelectedAnswer.setVisibility(View.VISIBLE);
            viewHolder.txtUserSelectedAnswer.setText(attempt.getUserAnswer());
            //viewHolder.txtAnswerDumy2.setText("Correct ans.");
            viewHolder.txtAnswerRight.setText(attempt.getCorrectAnswer());

            if(attempt.getUserselectedImageUrl()!=null&&!attempt.getUserselectedImageUrl().equals("") && attempt.getAnsImageUrl()!=null&&!attempt.getAnsImageUrl().equals("")){
                viewHolder.userSelectedImage.setVisibility(View.VISIBLE);
                Picasso.get().load(attempt.getUserselectedImageUrl()).into(viewHolder.userSelectedImage);
                viewHolder.rightAnsImage.setVisibility(View.VISIBLE);
                Picasso.get().load(attempt.getAnsImageUrl()).into(viewHolder.rightAnsImage);
            }
        }
        String viewColor = attempt.isWinOrLost() ? "#4CAF51" : "#F44336";
        viewHolder.viewLeft.setBackgroundColor(Color.parseColor(viewColor));
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attempt.getQusImageUrl()!=null &&!attempt.getQusImageUrl().equals("") ){
                    viewHolder.questionImage.setVisibility(View.VISIBLE);
                    Picasso.get().load(attempt.getQusImageUrl()).into(viewHolder.questionImage);
                }
                viewHolder.relativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //viewHolder.txtQuestion.setText(attempt.getQuestion());
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.dummytext1.setVisibility(View.VISIBLE);
                viewHolder.dummytext.setVisibility(View.GONE);
            }
        });
        viewHolder.txtSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Question.Options> options=new ArrayList<>();
                Question.Options option= new Question.Options();
                option.setOption(attempt.getCorrectAnswer());
                option.setOptionImgUrl(attempt.getAnsImageUrl());
                option.setOptionID("1");
                Question mQuestion= new Question();
                mQuestion.setId(attempt.getQuestionid());
                mQuestion.setQuestion(attempt.getQuestion());
                mQuestion.setQuestionImgUrl(attempt.getQusImageUrl());
                options.add(option);
                mQuestion.setOptions(options);
                DiscussionActivity.show(context, mQuestion);

            }
        });
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserSelectedAnswer,txtAnswerRight,txtQuestion;
       // TextView txtAnswer;
        View viewLeft;
        CardView cardView;
        TextView dummytext,dummytext1,txtAnswerDumy,
                txtAnswerDumy2;
        ImageView userSelectedImage,rightAnsImage,questionImage;
        TextView txtSolution;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = itemView.findViewById(R.id.txt_question);
            //txtAnswer = itemView.findViewById(R.id.txt_answer);
            txtAnswerRight = itemView.findViewById(R.id.txt_answer_right);
            txtAnswerDumy2 = itemView.findViewById(R.id.txt_answer_dumy2);
            txtUserSelectedAnswer = itemView.findViewById(R.id.txt_answer_worng);
            txtAnswerDumy = itemView.findViewById(R.id.txt_answer_dumy);
            questionImage = itemView.findViewById(R.id.question_image);
            rightAnsImage = itemView.findViewById(R.id.right_ans_image);
            userSelectedImage = itemView.findViewById(R.id.user_selected_image);
            viewLeft = itemView.findViewById(R.id.viewLeft);
            cardView = itemView.findViewById(R.id.card_view);
            dummytext = itemView.findViewById(R.id.dummytext);
            dummytext1 = itemView.findViewById(R.id.dummytext1);
            txtSolution = itemView.findViewById(R.id.txt_solution);
            linearLayout = itemView.findViewById(R.id.ans_layout);
            relativeLayout = itemView.findViewById(R.id.layout);
            //txtQuestion.intiliazeWebView();
        }
    }
}