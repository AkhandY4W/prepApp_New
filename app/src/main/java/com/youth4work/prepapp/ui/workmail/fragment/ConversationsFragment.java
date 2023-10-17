package com.youth4work.prepapp.ui.workmail.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;
import com.youth4work.prepapp.R;
import com.youth4work.prepapp.infrastructure.UserManager;
import com.youth4work.prepapp.network.model.User;
import com.youth4work.prepapp.network.model.response.BusinessObject;
import com.youth4work.prepapp.network.model.response.Chat;
import com.youth4work.prepapp.network.model.response.Message;
import com.youth4work.prepapp.ui.base.BaseFragment;
import com.youth4work.prepapp.ui.workmail.adapter.ConversationAdapter;
import com.youth4work.prepapp.ui.workmail.manager.FeedManager;
import com.youth4work.prepapp.ui.workmail.manager.FeedParams;
import com.youth4work.prepapp.ui.workmail.manager.Interfaces;
import com.youth4work.prepapp.util.Constants;
import com.youth4work.prepapp.util.UrlConstants;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ConversationsFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mLayoutId = R.layout.fragment_conversation;
    private EditText mEditText;
    private View parentView = null;
    private boolean isViewFirstCreated = false;
    ProgressRelativeLayout mProgressRelativeLayout;
    private String SenderUserId;
    ImageView btnSendMsg, btnSendFile;

    private ArrayList<Message> mArrListMessages = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = super.onCreateView(inflater, container, savedInstanceState);
            isViewFirstCreated = true;
            SenderUserId = mPreferencesManager.getid();
            containerView = setContentView(mLayoutId, parentView);
            btnSendMsg = parentView.findViewById(R.id.send_button);
            btnSendFile = parentView.findViewById(R.id.file_button);
            btnSendMsg.setOnClickListener(this);
            mEditText = parentView.findViewById(R.id.edit_text_answer);
            mProgressRelativeLayout = parentView.findViewById(R.id.progressActivity);
        } else {
            if (parentView.getParent() != null) {
                ((ViewGroup) parentView.getParent()).removeView(parentView);
            }
        }
        mProgressRelativeLayout.showLoading();
        mEditText.setOnFocusChangeListener((view, b) -> {
            if (mEditText.hasFocus()) {
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (mEditText.getText().toString().length() > 0) {
                            btnSendMsg.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_active_24dp));
                            btnSendFile.setImageDrawable(getResources().getDrawable(R.drawable.ic_attach_file_active_24dp));
                        } else {
                            btnSendMsg.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward_black));
                            btnSendFile.setImageDrawable(getResources().getDrawable(R.drawable.ic_attach_file_black_24dp));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

            }

        });
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mView = view;
        if (isViewFirstCreated) {
            initUiFirstTime();
            isViewFirstCreated = false;
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void initUiFirstTime() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = mView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mArrListMessages = new ArrayList<Message>();
        getMessagesByPage(1);
        mRecyclerView.smoothScrollToPosition(mArrListMessages.size());
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreData(current_page);
            }
        });
    }
    private int mPreviousPageNumber = 0;

    private void getMessagesByPage(final int pageNumber) {
        User currentUser = UserManager.getInstance(mContext).getUser();
        //if (currentUser.isLoggedIn()) {
            mProgressRelativeLayout.showContent();
            String userId = String.valueOf(currentUser.getUserId());
            String toUserId = SenderUserId;
            if (userId != null && toUserId != null) {
                String messagesUrl = UrlConstants.GET_CONVERSATION_URL
                        .replace("{myUserId}", userId).replace("{toUserId}", toUserId);
                messagesUrl += "&pageNumber=" + pageNumber;
                messagesUrl += "&pageSize=" + Constants.PAGE_LIMIT;

                if (mPreviousPageNumber == pageNumber)
                    return;

                FeedParams getFeedParams = new FeedParams(messagesUrl, Chat.class, new Interfaces.IDataRetrievalListener() {
                    @Override
                    public void onDataRetrieved(BusinessObject businessObject) {
                        mPreviousPageNumber = pageNumber;
                         if (null != businessObject && businessObject instanceof Chat && businessObject.getVolleyError() == null) {
                            ArrayList<Message> arrListMessagesByPage = ((Chat) businessObject).getArrListMessages();
                            if (arrListMessagesByPage != null && arrListMessagesByPage.size() > 0) {
                                mArrListMessages.addAll(arrListMessagesByPage);
                                for(int i=0;i<=arrListMessagesByPage.size()-1;i++) {
                                    if(mArrListMessages.get(i).getSenderName()!=null && !mArrListMessages.get(i).getSenderName().equals(""))
                                    { getActivity().setTitle(Constants.getFirstName(mArrListMessages.get(i).getSenderName()));
                                        break;}
                                    else {
                                        getActivity().setTitle("Conversation");
                                    }
                                }
                                 setupViews();
                                if (arrListMessagesByPage.size() < Constants.PAGE_LIMIT) {
                                    if (mRecyclerView != null)
                                        mRecyclerView.setOnScrollListener(null);
                                }
                            } else {
                                if (mRecyclerView != null)
                                    mRecyclerView.setOnScrollListener(null);
                            }
                        } else {
                            Toast.makeText(mContext, mResources.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                getFeedParams.setShouldCache(false);
                FeedManager.getInstance().queueJob(getFeedParams);
            }
        }
    //}

    private void loadMoreData(int currentPage) {
        getMessagesByPage(currentPage);
    }

  /*  @Override
    public void setActionBar() {
        super.setActionBar();
        ((YouthActivity)mContext).getSupportActionBar().setTitle(Constants.ACTION_BAR_TITLE_CONVERSATION);
        ((YouthActivity)mContext).setActionBarBackButtonVisibility(true);
    }*/

    private void setupViews() {
        if (mAdapter == null) {
            if (mArrListMessages != null && mArrListMessages.size() > 0) {
                mAdapter = new ConversationAdapter(mContext, mArrListMessages);
                mRecyclerView.setAdapter(mAdapter);
            }
        } else {
            mAdapter.setArrListMessages(mArrListMessages);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_button) {
            final Editable message = mEditText.getText();

            if (!TextUtils.isEmpty(message)) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("message", message);
                    Log.d("YTest", jsonObject.toString());
                    FeedParams feedParams = new FeedParams(UrlConstants.POST_CONVERSATION_URL + UserManager.getInstance(mContext).getUser().getUserId() + "/" + SenderUserId, String.class, null);
                    feedParams.setMethod(Request.Method.POST);
                    FeedManager.getInstance().queueJobMultipart(feedParams, jsonObject.toString(), new Interfaces.IDataRetrievalListenerString() {
                        @Override
                        public void onDataRetrieved(String string) {
                            try {
                                if (!TextUtils.isEmpty(string) && Integer.parseInt(string) >= 0) {
                                    Toast.makeText(mContext, "Message sent", Toast.LENGTH_SHORT).show();
                                    mEditText.setText("");

                                    Message message1 = new Message();
                                    message1.setMessageBody(message.toString());
                                    message1.setSenderUserId(String.valueOf(UserManager.getInstance(mContext).getUser().getUserId()));
                                    message1.setMessageID(string);
                                    /*long timeInMillis = System.currentTimeMillis();
                                    String date = "/Date(" + timeInMillis + "+0530)/";
                                    */
                                    Date curDate = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    String date = sdf.format(curDate);
                                    Log.d("YTestFragment", date);
                                    message1.setSentDate(date);
                                    mArrListMessages.add(message1);
                                    setupViews();
                                } else
                                    Toast.makeText(mContext, "Some  error occured. Please try again later", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(mContext, "Some  error occured. Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(mContext, mResources.getString(R.string.type_your_message), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
