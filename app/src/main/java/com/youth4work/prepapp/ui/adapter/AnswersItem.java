package com.youth4work.prepapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApi;
import com.youth4work.prepapp.network.PrepService;
import com.youth4work.prepapp.network.model.Answers;
import com.youth4work.prepapp.network.model.request.PostVote;
import com.youth4work.prepapp.network.model.response.VoteResponse;
import com.youth4work.prepapp.ui.forum.CommentsActivity;
import com.youth4work.prepapp.ui.forum.PrepForumDetails;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.PreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Samar on 5/13/2016.
 */
public class AnswersItem extends AbstractItem<AnswersItem, AnswersItem.ViewHolder> {
    public Answers answers;
    private PrepService prepService;
    Context context;
    int answerId;
    Long userId;

    public AnswersItem(Answers answers, Context context, int answerId, Long userId) {
        this.answers = answers;
        this.context = context;
        this.answerId = answerId;
        this.userId = userId;
    }

    public int getType() {
        return R.id.attempt_item_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.answers_layout2;
    }

    @Override
    public void bindView(ViewHolder viewHolder) {
        super.bindView(viewHolder);

        prepService = PrepApi.createService(PrepService.class, PreferencesManager.instance(context).getToken()[0]);

        viewHolder.txtAnswer.setText(Html.fromHtml(answers.getAnswer()));
        viewHolder.txtAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "By "+Constants.getFirstName(answers.getAnswerByName())+" • "+"Comments "+answers.getTotalComment();
        viewHolder.txtAnswerByName.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        viewHolder.txtVotes.setText(Integer.toString(answers.getARank()));
        //viewHolder.txtAnswerByName.setText(answers.getAnswerByName());
        //  viewHolder.txtTotalComment.setText("Comment "+ answers.getTotalComment());
        // viewHolder.txtAnswerByName.setText(String.valueOf("Vote "+answers.getARank()));
//        viewHolder.txtLastModified.setText((Constants.getDate(answers.getLastModified())));
        Picasso.get().load(answers.getAnswerByPic()).into(viewHolder.imageView);

        viewHolder.btnDn.setOnClickListener(v -> {
            if(answers.AnswerByUserId==userId)
            {
                Toast.makeText(context, "Can't vote on your own answer", Toast.LENGTH_SHORT).show();
            }
            else
            {

                prepService.VoteForumAnswer(new PostVote(answerId, false, userId)).enqueue(new Callback<VoteResponse>() {
                    @Override
                    public void onResponse(Call<VoteResponse> call, @NonNull Response<VoteResponse> response) {
                        if (response.isSuccessful()) {
                            Boolean check = response.body().isVoteForumAnswerResult();
                            if (check)
                                Toast.makeText(context, "Already voted", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(context, "Voted", Toast.LENGTH_SHORT).show();
                                viewHolder.txtVotes.setText(Integer.toString(answers.getARank() - 1));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<VoteResponse> call, Throwable t) {

                    }
                });
            }
        });
        viewHolder.formAnsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsAnswers = new Intent(context, CommentsActivity.class);
                commentsAnswers.putExtra("Answer id", answers.getAnswerId());
                commentsAnswers.putExtra("comment",answers.getAnswer());
                commentsAnswers.putExtra("commenter_name",answers.getAnswerByName());
                commentsAnswers.putExtra("comenter_img_url",answers.getAnswerByPic());
                context.startActivity(commentsAnswers);
            }
        });
        viewHolder.btnUp.setOnClickListener(v -> {
            if(answers.AnswerByUserId==userId)
            {
                Toast.makeText(context, "Can't vote on your own answer", Toast.LENGTH_SHORT).show();
            }
            else {
                prepService.VoteForumAnswer(new PostVote(answerId, true, userId)).enqueue(new Callback<VoteResponse>() {
                    @Override
                    public void onResponse(Call<VoteResponse> call, @NonNull Response<VoteResponse> response) {
                        if(response.isSuccessful()) {
                            Boolean check = response.body().isVoteForumAnswerResult();
                            if (check)
                                Toast.makeText(context, "Already voted", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(context, "Voted", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Voted", Toast.LENGTH_SHORT).show();
                                viewHolder.txtVotes.setText(Integer.toString(answers.getARank() + 1));
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<VoteResponse> call, Throwable t) {

                    }
                });
            }
        });


    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAnswer;
        TextView txtAnswerByName;
        TextView txtVotes;
        //      TextView txtLastModified;
        LinearLayout btnUp;
        ImageView btnDn;
        CircularImageView imageView;
        CardView formAnsCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAnswer = itemView.findViewById(R.id.answer);
            txtAnswerByName = itemView.findViewById(R.id.AnswerByName);
            txtVotes = itemView.findViewById(R.id.totalvotes);
//          txtLastModified = (TextView) itemView.findViewById(R.id.LastModified);
            // txtARank = itemView.findViewById(R.id.ARank);
            imageView = itemView.findViewById(R.id.imageAnswer);
            btnUp =  itemView.findViewById(R.id.up);
            btnDn = itemView.findViewById(R.id.down);
            formAnsCard = itemView.findViewById(R.id.form_ans_card);

        }
    }
}