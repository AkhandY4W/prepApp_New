package com.youth4work.prepapp.network;

import androidx.annotation.NonNull;

import com.youth4work.prepapp.network.model.AllMockQSModel;
import com.youth4work.prepapp.network.model.Attempt;
import com.youth4work.prepapp.network.model.Category;
import com.youth4work.prepapp.network.model.Comments;
import com.youth4work.prepapp.network.model.CommentsAnswersData;
import com.youth4work.prepapp.network.model.CouponCode;
import com.youth4work.prepapp.network.model.EducationDetails;
import com.youth4work.prepapp.network.model.ForumDetails;
import com.youth4work.prepapp.network.model.ParentCategory;
import com.youth4work.prepapp.network.model.Plan;
import com.youth4work.prepapp.network.model.PrepForum;
import com.youth4work.prepapp.network.model.Question;
import com.youth4work.prepapp.network.model.Rank;
import com.youth4work.prepapp.network.model.Section;
import com.youth4work.prepapp.network.model.Subject;
import com.youth4work.prepapp.network.model.SubjectStats;
import com.youth4work.prepapp.network.model.TestResult;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.network.model.UserStats;
import com.youth4work.prepapp.network.model.request.CommentOnForumAnswerRequest;
import com.youth4work.prepapp.network.model.request.CommentRequest;
import com.youth4work.prepapp.network.model.request.ForumAnswerRequest;
import com.youth4work.prepapp.network.model.request.ForumRequest;
import com.youth4work.prepapp.network.model.request.GcmRegister;
import com.youth4work.prepapp.network.model.request.LoginRequest;
import com.youth4work.prepapp.network.model.request.PostVote;
import com.youth4work.prepapp.network.model.request.PushAnswer;
import com.youth4work.prepapp.network.model.request.UserRegister;
import com.youth4work.prepapp.network.model.request.UserRegisterSocial;
import com.youth4work.prepapp.network.model.request.UserUpgrade;
import com.youth4work.prepapp.network.model.response.LoginResponse;
import com.youth4work.prepapp.network.model.response.SubCatDetailsItem;
import com.youth4work.prepapp.network.model.response.UserAllow;
import com.youth4work.prepapp.network.model.response.VoteResponse;
import com.youth4work.prepapp.network.model.response.YouthListResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface PrepService {

    @NonNull
    @POST("authenticate")
    Call<LoginResponse> getAuth(@Body LoginRequest loginRequest);

    @NonNull
    @GET("getPrepCategory?pageNumber=1&pageSize=100")
    Call<List<Category>> getCategories();

    @NonNull
    @GET("getPrepSubCategory")
    Call<List<Category>> subCategories(
            @Query("CatID") int categoryId,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getSubCategoryDetails")
    Call<SubCatDetailsItem> getSubCatDetails(
            @Query("Subcatid") int Subcatid);

    @NonNull
    @GET("getMockPrepSubject")
    Call<List<Section>> mockPrepSubject(
            @Query("subcatid") int subcatid,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getAllMockQS")
    Call<AllMockQSModel> getAllMockQS(
            @Query("testid") int testId,
            @Query("userid") Long UserId
    );

    @NonNull
    @POST("SubmitTest")
    Call<Boolean> SubmitTest(
            @Query("testid") int testid,
            @Query("userid") Long userId

    );

    @NonNull
    @GET("canAttemptMockQns")
    Call<UserAllow> canAttemptMockTest(
            @Query("Userid") Long userId,
            @Query("TestID") int testId);

    @NonNull
    @POST("updateAnswer4PrepQs")
    Call<ResponseBody> updateAnswer(@Body PushAnswer answer);

    @NonNull
    @POST("StartTest")
    Call<Integer> StartTest(
            @Query("testid") int testid,
            @Query("userid") Long userId

    );

    @NonNull
    @GET("doLogin")
    Call<User> login(
            @Query("u") String username,
            @Query("p") String password);

    @NonNull
    @GET("LoginWithSocialID")
    Call<User> socialLogin(
            @Query("u") String username,
            @Query("for") String fbOrGp);

    @NonNull
    @GET("getPrepSubCategory?CatID=0&pageNumber=1&pageSize=5")
    Observable<List<Category>> popularExams();

    @NonNull
    @GET("getPrepSubCategory?CatID=0&pageNumber=1&pageSize=5")
    Call<List<Category>> getPopularExams();

    @NonNull
    @GET("getPrepCategory?pageNumber=1&pageSize=100")
    Observable<List<Category>> categories();

    @NonNull
    @GET("getprepcategorySubCategoryList?pageNumber=1&pageSize=100")
    Call<List<ParentCategory>> getcategories();

    @NonNull
    @GET("getPrepTestBySubCat")
    Call<List<Subject>> subjects(
            @Query("subcatid") int categoryId);

    @NonNull
    @GET("getExamScore")
    Observable<UserStats> userStats(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId);

    @NonNull
    @GET("getStats")
    Observable<UserStats> userStats(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId,
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate);

    @NonNull
    @GET("getQuestionsFaced")
    Observable<List<Attempt>> getprepQsAttempt(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId,
            @Query("onDate") String onDate,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getQuestionsFaced")
    Call<List<Attempt>> getNextprepQsAttempt(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId,
            @Query("onDate") String onDate,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getprepTestResult")
    Observable<TestResult> prepTestResult(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId,
            @Query("onDate") String onDate,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getMyPrepList")
    Call<List<Category>> getMyPrepList(
            @Query("Userid") Long userId,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("allCollegesOnY4W")
    Observable<List<String>> colleges();

    @NonNull
    @GET("allDegreeOnY4W")
    Observable<List<String>> degrees();

    @NonNull
    @GET("allSpecOnY4W")
    Observable<List<String>> specializations();

    @NonNull
    @GET("allCityOnY4W")
    Observable<List<String>> cities();

    @NonNull
    @GET("isEmailIdExists/{email}")
    Call<ResponseBody> emailIDExists(
            @Path("email") String email);

    @NonNull
    @GET("isMobileExists/{mobileno}")
    Call<ResponseBody> mobileExists(
            @Path("mobileno") String mobileno);

    @NonNull
    @GET("isUserNameExists/{username}")
    Call<ResponseBody> usernameExists(
            @Path("username") String username);

    @NonNull
    @POST("doRegister")
    Call<User> register(@Body UserRegister userRegister);

    @NonNull
    @POST("doRegisterWithSocialSite")
    Call<ResponseBody> registerSocial(@Body UserRegisterSocial userRegister);

    @NonNull
    @POST("doRegisterGcmUser")
    Call<ResponseBody> registerGcm(@Body GcmRegister gcmRegister);

    @NonNull
    @POST("doUpgrade")
    Call<ResponseBody> upgradePlan(@Body UserUpgrade userUpgrade);

    @NonNull
    @POST("pushAnswer4PrepQs")
    Call<ResponseBody> pushAnswer(@Body PushAnswer answer);

    @NonNull
    @GET("getCommentonQuestion/{questionId}")
    Call<Comments> comments(
            @Path("questionId") int questionId);

    @NonNull
    @POST("insertCommentOnprepTestQs")
    Call<ResponseBody> postComment(@Body CommentRequest commentRequest);

    @NonNull
    @GET("getAttemptedOnDate")
    Call<List<String>> attemptOnDate(
            @Query("Userid") Long userId,
            @Query("SubCatID") int categoryId,
            @Query("fromDate") String fromDate,
            @Query("toDate") String toDate);

    @NonNull
    @GET("canAttemptTestToday")
    Call<UserAllow> canAttempt(
            @Query("Userid") Long userId,
            @Query("TestID") int testId,
            @Query("TestType") int testType);

    @NonNull
    @GET("forgotPWD/{email}")
    Call<ResponseBody> forgotPassword(
            @Path("email") String email);

    @NonNull
    @GET("rankBySubject")
    Call<List<Rank>> ranksByCategory(
            @Query("SubjectId") int categoryId,
            @Query("pageNumber") int pageNo,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getPrepServicePack/{couponcode}")
    Call<List<Plan>> plans(@Path("couponcode") String couponcode,
                           @Query("UserID") Long userId);

    @NonNull
    @POST("doUpgradeOn100pDis")
    Call<ResponseBody> doUpgradeOn100pDis(@Body UserUpgrade userUpgrade);

    @NonNull
    @GET("getSubjectStats")
    Observable<List<SubjectStats>> subjectStats(
            @Query("Userid") Long userId,
            @Query("SubCatID") int catId);


    @NonNull
    @GET("GetForumAnswerComment")
    Call<List<CommentsAnswersData>> commentsAnswer(
            @Query("answerId") int answerId,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @POST("PostForum")
    Call<ResponseBody> PostForum(@Body ForumRequest forumRequest);

    @NonNull
    @POST("VoteForumAnswer")
    Call<VoteResponse> VoteForumAnswer(@Body PostVote postVote);

    @NonNull
    @POST("PostForumAnswer")
    Call<ResponseBody> PostForumAnswer(@Body ForumAnswerRequest forumAnswerRequest);

    @NonNull
    @POST("EditForumAnswer")
    Call<ResponseBody> EditForumAnswer(@Body com.youth4work.prepapp.network.model.request.ForumAnswerEditRequest forumAnswerEditRequest);


    @NonNull
    @POST("CommentOnForumAnswer")
    Call<ResponseBody> CommentOnForumAnswer(@Body CommentOnForumAnswerRequest commentOnForumAnswerRequest);

    @NonNull
    @GET("getprepForums")
    Call<List<PrepForum>> prepForum(
            @Query("testid") int testid,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);


    @NonNull
    @GET("GetForumDetail")
    Call<ForumDetails> prepForumDetails(
            @Query("Forumid") int Forumid,
            @Query("UserId") Long UserId);

    @NonNull
    @GET("getPrepSubject")
    Call<List<Section>> sections(
            @Query("subcatid") int subcatid,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);

    @NonNull
    @GET("getPrepTest")
    Call<List<Subject>> prepTests(
            @Query("SubjectID") int SubjectID,
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize);


    @NonNull
    @GET("getPrepQuestion")
    Call<Question> question(
            @Query("Prev_w_l_Count") int prevWinLoseCount,
            @Query("Prev_winOrLose") int winOrLose,
            @Query("Prev_Score") double prevScore,
            @Query("testid") int randomTestId,
            @Query("UserId") Long userId);

    @NonNull
    @GET("getPrepSumQuestion")
    Call<Question> sum_question(
            @Query("Prev_w_l_Count") int prevWinLoseCount,
            @Query("Prev_winOrLose") int winOrLose,
            @Query("Prev_Score") double prevScore,
            @Query("testid") int randomTestId,
            @Query("UserId") Long userId);

    @NonNull
    @GET("getCoupons4App")
    Call<List<CouponCode>> couponcode();

    @NonNull
    @POST("getOTP")
    Call<Boolean> getOtpCode(
            @Query("userid") Long userId,
            @Query("contactno") String contactNo
    );

    @NonNull
    @POST("verifyYMobile")
    Call<Integer> verifyYMobile(
            @Query("userid") Long userId,
            @Query("contactno") String contactNo,
            @Query("code") String code
    );

    @NonNull
    @POST("SendverificationLink")
    Call<ResponseBody> SendverificationLink(
            @Query("emailid") String emailId
    );

    @NonNull
    @GET("getYCurrentEducation")
    Call<EducationDetails> geteducationdetails(
            @Query("Userid") Long userid
    );

    @NonNull
    @POST("UpdateYCurrentEducation")
    Call<ResponseBody> UpdateYCurrentEducation(
            @Body EducationDetails edt);

    @NonNull
    @GET("GetPrepTestYouthList")
    Call<YouthListResponse> getYouthList(
            @Query("testid") int testid,
            @Query("userid") long userid,
            @Query("start") int pageNo,
            @Query("end") int pageSize,
            @Query("flag") String flag);
}
