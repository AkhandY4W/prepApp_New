package com.youth4work.prepapp.ui.public_profile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.network.PrepApiFory4w;
import com.youth4work.prepapp.network.PrepServiceFory4w;
import com.youth4work.prepapp.network.model.response.user_profile.ProfileData;
import com.youth4work.prepapp.network.model.response.user_profile.TalentProfileModel;
import com.youth4work.prepapp.network.model.response.user_profile.YouthEducationDetailsModel;
import com.youth4work.prepapp.network.model.response.user_profile.YouthWorkDetailsModel;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.home.NoInternetActivity;
import com.youth4work.prepapp.ui.public_profile.ProfileActivity;
import com.youth4work.prepapp.ui.public_profile.adapter.TalentProfileAdapter;
import com.youth4work.prepapp.ui.public_profile.adapter.WorkDetailsAdapter;
import com.youth4work.prepapp.ui.public_profile.adapter.YouthEducationAdapter;
import com.youth4work.prepapp.ui.public_profile.adapter.YouthProjectAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends BaseFragment {

    ProfileData profileData;
    Button btnSeeResume;
    LinearLayout viewResume;
    TextView summaryDetail, careerAimDeatils, emailId,
            phoneNumber, gender, DOB,
            address, pinCode, txtResume;
    CardView summaryCard, careerAimCard,
            cardWorkDetails, cardProject,
            educationCard, talentCard;
    ProgressRelativeLayout progressActivity;
    RecyclerView mWorkDetailsRecyclerView, mProjectRecycler,
            mEducationRecycler, mTalentRecycler;
    PrepServiceFory4w prepServiceFory4w;
    long mUserId;
    public UserProfile(long userId) {
        mUserId=userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment

        View itemView = inflater.inflate(
                R.layout.profile, container, false);
        ButterKnife.bind(this, itemView);
        pinCode = itemView.findViewById(R.id.pin_code);
        careerAimCard = itemView.findViewById(R.id.career_aim_card);
        cardWorkDetails = itemView.findViewById(R.id.card_work_details);
        cardProject = itemView.findViewById(R.id.card_project);
        educationCard = itemView.findViewById(R.id.education_card);
        talentCard = itemView.findViewById(R.id.talent_card);
        summaryCard = itemView.findViewById(R.id.summary_card);
        address = itemView.findViewById(R.id.address);
        DOB = itemView.findViewById(R.id.DOB);
        gender = itemView.findViewById(R.id.gender);
        phoneNumber = itemView.findViewById(R.id.phone_number);
        emailId = itemView.findViewById(R.id.email_id);
        careerAimDeatils = itemView.findViewById(R.id.career_aim_deatils);
        summaryDetail = itemView.findViewById(R.id.summary_detail);
        progressActivity = itemView.findViewById(R.id.progress_activity);
        mWorkDetailsRecyclerView = itemView.findViewById(R.id.work_details_recycler);
        mProjectRecycler = itemView.findViewById(R.id.project_recycler);
        mEducationRecycler = itemView.findViewById(R.id.education_recycler);
        mTalentRecycler = itemView.findViewById(R.id.talent_recycler);
        viewResume = itemView.findViewById(R.id.layout_resume_view);
        txtResume = itemView.findViewById(R.id.txt_resume_view);
        btnSeeResume = itemView.findViewById(R.id.btn_resume);
        prepServiceFory4w= PrepApiFory4w.provideRetrofit();
        progressActivity.showLoading();
        prepServiceFory4w.GetProfile(mUserId).enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, @NonNull Response<ProfileData> response) {
                if (response.isSuccessful() && response.code()== 200 && response.body() != null) {
                    profileData = response.body();
                    fillData();
                    String location = profileData.getLocation();
                    if(profileData!=null && location!=null && profileData.getHeadLine()!=null)
                     ((ProfileActivity) getActivity()).setText_location(location, profileData.getHeadLine());
                    else {
                        Intent intent = new Intent(getContext(), NoInternetActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(self, NoInternetActivity.class);
                    self.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {

            }
        });
       getWorkDetails();
       getEducationDetails();
        getTalentProfile();

        /*button_more_summary.setOnClickListener(view1 -> {
            float deg = button_more_summary.getRotation() + 180F;
            button_more_summary.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            if (check1) {
                summary_detail.setMaxLines(15);
                check1 = !check1;
            } else {
                summary_detail.setMaxLines(3);
                check1 = !check1;
            }
        });
        career_aim_more_deatils.setOnClickListener(view1 -> {
            float deg = career_aim_more_deatils.getRotation() + 180F;
            career_aim_more_deatils.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            if (check) {
                career_aim_deatils.setMaxLines(15);
                check = !check;
            } else {
                career_aim_deatils.setMaxLines(3);
                check = !check;
            }
        });
        button_for_more_work_detail.setOnClickListener(view1 -> {
            float deg = button_for_more_work_detail.getRotation() + 180F;
            button_for_more_work_detail.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            if (check2) {
                key_responsibilities.setMaxLines(15);
                check2 = !check2;
            } else {
                key_responsibilities.setMaxLines(3);
                check2 = !check2;
            }
        });
        edit_summary.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_summary", Toast.LENGTH_SHORT).show();
        });
        edit_career_aim.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_career_aim", Toast.LENGTH_SHORT).show();
        });
        edit_in_course.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_in_course", Toast.LENGTH_SHORT).show();
        });
        add_education_details.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "add_education_details", Toast.LENGTH_SHORT).show();


        });

        add_work_experience.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "add_work_experience", Toast.LENGTH_SHORT).show();


        });

        edit_work_experience.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_work_experience", Toast.LENGTH_SHORT).show();


        });

        add_project.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "add_project", Toast.LENGTH_SHORT).show();


        });

        edit_personal_details.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_personal_details", Toast.LENGTH_SHORT).show();
        });
        edit_project.setOnClickListener(view2 -> {
            Toast.makeText(getActivity(), "edit_project", Toast.LENGTH_SHORT).show();
        });*/
        return itemView;
    }

    private void getEducationDetails() {

        prepServiceFory4w.GetEducationProfile(mUserId).enqueue(new Callback<YouthEducationDetailsModel>() {
            @Override
            public void onResponse(Call<YouthEducationDetailsModel> call, Response<YouthEducationDetailsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    YouthEducationAdapter mYouthEducationAdapter;
                    YouthEducationDetailsModel mYouthEducationDetails;
                    mYouthEducationDetails = response.body();
                    if (mYouthEducationDetails != null) {
                        progressActivity.showContent();
                        educationCard.setVisibility(View.VISIBLE);
                        LinearLayoutManager llm = new LinearLayoutManager(self);
                        mEducationRecycler.setHasFixedSize(true);
                        mEducationRecycler.setLayoutManager(llm);
                        mYouthEducationAdapter = new YouthEducationAdapter(self, mYouthEducationDetails);
                        mEducationRecycler.setAdapter(mYouthEducationAdapter);
                    } else {
                        progressActivity.showContent();
                        educationCard.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(Call<YouthEducationDetailsModel> call, Throwable t) {
                Log.e("Education error", t + "");
                educationCard.setVisibility(View.GONE);
                progressActivity.showContent();
                Toast.makeText(self, "Something went wrong, please try again" + t, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getWorkDetails() {

        prepServiceFory4w.getWorkprofile(mUserId).enqueue(new Callback<YouthWorkDetailsModel>() {
            @Override
            public void onResponse(Call<YouthWorkDetailsModel> call, Response<YouthWorkDetailsModel> response) {
                YouthWorkDetailsModel youthWorkDetailsModel;
                WorkDetailsAdapter mWorkDetailsAdapter;
                YouthProjectAdapter mYouthProjectAdapter;
                if (response.isSuccessful() && response.body() != null) ;
                youthWorkDetailsModel = response.body();
                progressActivity.showContent();
                Log.e("WorkDetails", String.valueOf(youthWorkDetailsModel));
                if (youthWorkDetailsModel.getWorkDetails().size() != 0) {
                    cardWorkDetails.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(self);
                    mWorkDetailsAdapter = new WorkDetailsAdapter(self, youthWorkDetailsModel.getWorkDetails());
                    mWorkDetailsRecyclerView.setHasFixedSize(true);
                    mWorkDetailsRecyclerView.setLayoutManager(llm);
                    mWorkDetailsRecyclerView.setAdapter(mWorkDetailsAdapter);
                } else {
                    progressActivity.showContent();
                    cardWorkDetails.setVisibility(View.GONE);

                }
                if (youthWorkDetailsModel.getYouthProjects().size() > 0) {
                    progressActivity.showContent();
                    cardProject.setVisibility(View.VISIBLE);
                    LinearLayoutManager llm = new LinearLayoutManager(self);
                    mYouthProjectAdapter = new YouthProjectAdapter(self, youthWorkDetailsModel.getYouthProjects());
                    mProjectRecycler.setHasFixedSize(true);
                    mProjectRecycler.setLayoutManager(llm);
                    mProjectRecycler.setAdapter(mYouthProjectAdapter);
                } else {
                    progressActivity.showContent();
                    cardProject.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<YouthWorkDetailsModel> call, Throwable t) {
                progressActivity.showContent();
            }
        });

    }

    private void getTalentProfile() {
        prepServiceFory4w.getTalentProfile(mUserId, 1, 50).enqueue(new Callback<ArrayList<TalentProfileModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TalentProfileModel>> call, Response<ArrayList<TalentProfileModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<TalentProfileModel> mTalentProfileModel = response.body();
                    TalentProfileAdapter talentProfileAdapter;
                    Log.d("mTalentProfileModel", "" + mTalentProfileModel);
                    if (mTalentProfileModel != null) {
                        progressActivity.showContent();
                        talentCard.setVisibility(View.VISIBLE);
                        talentProfileAdapter = new TalentProfileAdapter(self, mTalentProfileModel);
                        LinearLayoutManager llm = new LinearLayoutManager(self);
                        mTalentRecycler.setHasFixedSize(true);
                        mTalentRecycler.setLayoutManager(llm);
                        mTalentRecycler.setAdapter(talentProfileAdapter);
                    } else {
                        progressActivity.showContent();
                        talentCard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TalentProfileModel>> call, Throwable t) {
                Log.e("Education error", t + "");
                educationCard.setVisibility(View.GONE);
                progressActivity.showContent();
                Toast.makeText(self, "Something went wrong, please try again" + t, Toast.LENGTH_LONG).show();

            }
        });

    }

    void fillData() {
        if (profileData.getAim() == "") {
            careerAimCard.setVisibility(View.GONE);
        } else {

            careerAimDeatils.setText(profileData.getAim());
        }
        if (profileData.getAbout() == "") {
            summaryCard.setVisibility(View.GONE);
        } else {
            summaryDetail.setText(profileData.getAbout());
        }
        emailId.setText(profileData.getEmailId());

        if (profileData.getGender().equals("O")) {
            gender.setText("Other");
        } else {
            gender.setText(profileData.getGender().equals("M") ? "Male" : "Female");
        }
        address.setText(profileData.getAddress());
        pinCode.setText(profileData.getAddressPinCode());
        phoneNumber.setText(profileData.getMobileNo());
        DOB.setText(profileData.getBirthDate());
    }
    
}

