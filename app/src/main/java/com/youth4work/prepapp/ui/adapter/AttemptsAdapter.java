package com.youth4work.prepapp.ui.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.model.Attempt;

import java.util.List;

public class AttemptsAdapter extends RecyclerView.Adapter<AttemptsAdapter.AttemptViewHolder> {

    private List<Attempt> attempts;

    public AttemptsAdapter(List<Attempt> attempts) {
        this.attempts = attempts;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public AttemptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_attempt, viewGroup, false);
        AttemptViewHolder pvh = new AttemptViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptViewHolder attemptViewHolder, int i) {
        attemptViewHolder.txtQuestion.setText(Html.fromHtml("Q. " + attempts.get(i).getQuestion()));
        attemptViewHolder.txtAnswer.setText(Html.fromHtml(attempts.get(i).isWinOrLost() ? "A. " : "C. " + attempts.get(i).getCorrectAnswer()));

        String viewColor = attempts.get(i).isWinOrLost() ? "#4CAF51" : "#F44336";
        attemptViewHolder.viewLeft.setBackgroundColor(Color.parseColor(viewColor));
        attemptViewHolder.viewRight.setBackgroundColor(Color.parseColor(viewColor));
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    public static class AttemptViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion;
        TextView txtAnswer;
        View viewLeft;
        View viewRight;

        AttemptViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = (TextView) itemView.findViewById(R.id.txt_question);
            //txtAnswer = (TextView) itemView.findViewById(R.id.txt_answer);
            viewLeft = itemView.findViewById(R.id.viewLeft);
           // viewRight = itemView.findViewById(R.id.viewRight);
        }
    }
}
