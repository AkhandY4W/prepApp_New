package com.youth4work.prepapp.ui.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.youth4work.prepapp.R;

public class RatingActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    private TextView txtRating;
    Button btnCancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        btnCancle = findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(view -> finish());
        addListenerOnRatingBar();
        addListenerOnButton();
    }
    public void addListenerOnRatingBar() {
        ratingBar = findViewById(R.id.ratingBar);
        txtRatingValue = findViewById(R.id.txtRatingValue);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            txtRatingValue.setText(String.valueOf(rating));
            if (ratingBar1.getRating() < 4 )
                {
                txtRating.setText("Thanks for Rating\n Please give feedback to improve the app");
            }
            else
                {
                txtRating.setText(" So glad you love the app \n Please give me rate on Play store");
            }
        });
    }
    public void addListenerOnButton() {
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtRating = findViewById(R.id.txtRating);
        btnSubmit.setOnClickListener(v -> {
            if (ratingBar.getRating() <4) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto: appsupport@youth4work.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                finish();
            } else {
                String url = "https://play.google.com/store/apps/details?id=" + RatingActivity.this.getPackageName();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                finish();
            }
        });
    }
}