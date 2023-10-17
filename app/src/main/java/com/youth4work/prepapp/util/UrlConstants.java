package com.youth4work.prepapp.util;

public class UrlConstants {

    //public final static String BASE_URL = "https://rest-services.youth4work.com/";
    public final static String BASE_URL = "https://apis.youth4work.com/";
    public final static String DOMAIN_NAME = "http://www.youth4work.com";
    public final static String USERNAME = "YOUTH4WORKAPP";
    public final static String PASSWORD = "YOUTH4WORK@14FEB";


    //Home Page Urls
    public final static String GET_JOB_URL = BASE_URL + "Android/yJobbyPage?ShowRelatedTalentJobs=true&userid={userid}";
    public final static String GET_YSTORY_URL = BASE_URL + "Android/yFeedbyPage?userid={userid}";
    public final static String GET_DISCUSSION_URL = BASE_URL + "Android/yDiscussionbyPage?userid={userid}";

    //Job Details
    public final static String JOB_DETAIL_URL = BASE_URL + "Android/getYjobDetail?jobid={jobid}&UserID={userid}";
    public final static String JOB_APPLY_URL = BASE_URL + "Android/doApply/{jobid}/{userid}";

    //Discussions
    public final static String DISCUSSION_DETAIL_ANSWER = BASE_URL + "Android/yDiscussionAnswers?forumId={forumId}";

    public static final String KEY_PARAMETER_POST = "parameter";
    public static final String KEY_COMMENT = "Comment";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_ANSWER = "Answer";
    public static final String KEY_QUESTIONID = "Questionid";

    public static final String KEY_USERID = "userid";
    public static final String KEY_DEVICE_ID = "DeviceId";
    public static final String KEY_GCM_TOKEN = "GcmTokenID";
    public static final String KEY_EMAILID = "emailid";

    public static final String autoSuggestDegreeUrl = BASE_URL+"Android/searchCourses/";
    public static final String autoSuggestSpecUrl = BASE_URL+"Android/searchSpec/";
    public static final String autoSuggestLocationUrl = BASE_URL+"Android/searchLocations/";
    public static final String autoSuggesCollegeUrl  = BASE_URL+"Android/searchColleges/";
    public static final String autoSuggestTalentUrl = BASE_URL+"Android/searchTalents/";

    public static final String validateUsernameUrl = BASE_URL+"Android/isUserNameExists/";
    public static final String validateEmailUrl = BASE_URL+"Android/isEmailIDExists/";
    public static final String validatePhonenoUrl = BASE_URL+"Android/isMobileExists/";

    public static final String registerUrl = BASE_URL+"Android/doRegister";
    public static final String signInUrl = BASE_URL+"Android/?u=<username>&p=<password>";
    public static final String updateTalentUrl = BASE_URL + "Android/addTalent4Youth/";
    public static final String getTalentUrl = BASE_URL + "Android/getUserTalent?UserId=";

    //Comments and Posts Urls
    public static final String POST_TALENT_FORUM_ANSWER = BASE_URL + "Android/PostTalentForumAnswer/{ForumId}/{usertype}/{AnswerByUserId}";
    public static final String COMMENT_ON_TALENT_FORUM = BASE_URL + "Android/CommentOnTalentForumAnswer/{AnswerId}/{CommentByUserId}";
    public static final String VOTE_UP_TALENT_FORUM = BASE_URL + "Android/VoteTalentForumAnswer/{answerId}/{userId}/{vote}";
    public static final String GET_ALL_COMMENTS_ON_ANSWER = BASE_URL + "Android/yDiscussionAnsCom?answerId={answerId}";


    //Notifications and Messages - POST APIs
    public static final String READ_NOTIFICATIONS_HEADER_COUNTER_URL = BASE_URL + "Android/readNot4HideHeaderCount/{userid}";
    public static final String READ_MESSAGE_HEADER_COUNTER_URL = BASE_URL + "Android/readMsg4HideHeaderCount/{userid}";

    //Notifications and Messages - GET APIs
    public static final String GET_MESSAGES_URL = BASE_URL + "Android/yMail?userid={userid}&isCount=false";
    public static final String GET_CONVERSATION_URL = BASE_URL + "Android/yConversations?myUserId={myUserId}&toUserId={toUserId}";

    public static final String GET_NOTIFICATION_MESSAGES_COUNT = BASE_URL + "Android/getNoOfNewMessages_Not/{userid}";
    public static final String GET_USER_NOTIFICATIONS_URL = BASE_URL + "Android/getUserNotification/{userid}";
    public static final String REGISTER_GCM_TOKEN = BASE_URL + "Android/doRegisterGcmUser";
    public static final String READ_MESSAGE_URL = BASE_URL + "Android/markAsReadMessages/{messageid}";


    public static final String FORGOT_PASSWORD_URL = BASE_URL+"Android/forgotPWD/";

    public static final String POST_CONVERSATION_URL = BASE_URL+"Android/sendyMail/";

    public static final String GET_COMMENT_ON_YSTORIES_URL = BASE_URL + "Android/yFeedComments/{category}?feedid={feedid}";
    public static final String COMMENT_POST_ON_YSTORY_URL = BASE_URL + "Android/CommentOnyFeed/{feedid}/{userid}";

    public static final String GET_YTEST_CATEGORIES_URL = BASE_URL + "Android/getYtestCategory?";
    public static final String GET_YTEST_BY_CATEGORY_URL = BASE_URL + "Android/getyTestByCat?CatId={CatId}";

    public static final String GET_QUESTION = BASE_URL + "Android/getQuestion?";
    public static final String SUBMIT_ANSWER = BASE_URL + "Android/pushAnswer4Qs/{optionChoosen}/{questionid}/{answerby}/{result}/{defaulttime_in_sec}/{timetaken_in_sec}";
    public static final String SUBMIT_QUESTION_OPINION = BASE_URL + "Android/LikeDislikeyTestQs/{QuestionId}/{opinionId}/{UserId}";

    public static final String TEST_SCORE = BASE_URL + "Android/getyTestResult?UserId={UserId}&TalentID={TalentID}";

    public static final String GET_COMMENTS_ON_QUESTION = BASE_URL + "Android/GetComOnyTestQs?fkquestionid={questionid}";
    public static final String POST_COMMENT_ON_QUESTION = BASE_URL + "Android/insertCommentOnyTestQs";

    public static final String UPDATE_PUSH_NOTIFICATION_SERVER = BASE_URL + "Android/doRegisterGcmUser";


}
