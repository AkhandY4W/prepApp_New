package com.youth4work.prepapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener
import com.vlonjatg.progressactivity.ProgressRelativeLayout
import com.youth4work.prepapp.R
import com.youth4work.prepapp.network.PrepApi
import com.youth4work.prepapp.network.PrepService
import com.youth4work.prepapp.network.model.response.Youth
import com.youth4work.prepapp.network.model.response.YouthListResponse
import com.youth4work.prepapp.ui.adapter.ChatUserListingAdapter
import com.youth4work.prepapp.ui.base.BaseActivity
import com.youth4work.prepapp.ui.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : BaseFragment() {
    lateinit var prepService: PrepService
    lateinit var youthList: ArrayList<Youth>
    lateinit var adapter:ChatUserListingAdapter
    lateinit var llm: LinearLayoutManager
    lateinit var recyclerChatUser: RecyclerView
    lateinit var progressActiviity:ProgressRelativeLayout
    var page:Int=1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_chat, container, false)
        prepService=PrepApi.createService(PrepService::class.java,
            mPreferencesManager.token?.get(0)
        )
        recyclerChatUser=view.findViewById(R.id.recycler_chat_user)
        progressActiviity=view.findViewById(R.id.progress_activiity)
        llm= LinearLayoutManager(self)
        getUserList()
        return view
    }

    private fun getUserList() {
        progressActiviity.showLoading()
        val call= prepService.getYouthList(mUserManager.category.catid,mUserManager.user.userId,page,20,"nodefaultimage")
        call.enqueue(object:Callback<YouthListResponse>{
            override fun onResponse(
                call: Call<YouthListResponse>,
                response: Response<YouthListResponse>
            ) {
                if(response.isSuccessful&&response.code()==200){
                    val youthListResponse= response.body()!!
                    youthList= youthListResponse.youthList as ArrayList<Youth>
                    progressActiviity.showContent()
                    initalizedAdapter(youthListResponse)
                }
                else{
                    progressActiviity.showContent()
                    Toast.makeText(
                        self,"Something went wrong,please try again...",
                        Toast.LENGTH_SHORT ).show()
                }
            }

            override fun onFailure(call: Call<YouthListResponse>, t: Throwable) {
                progressActiviity.showContent()
                Log.e("Error", t.message.toString())
            }
        })
        recyclerChatUser.addOnScrollListener(object : EndlessRecyclerOnScrollListener(llm){
            override fun onLoadMore(currentPage: Int) {
                getUserList(page++)
            }
        })
    }
    private fun initalizedAdapter(youthListResponse: YouthListResponse) {
        recyclerChatUser.layoutManager=llm
        adapter= ChatUserListingAdapter(self, youthListResponse.youthList as ArrayList<Youth>)
        recyclerChatUser.adapter=adapter
       // adapter.notifyDataSetChanged()
    }
    private fun getUserList(page:Int) {
        val call= prepService.getYouthList(mUserManager.category.catid, mUserManager.user.userId,page,20,"")
        call.enqueue(object :Callback<YouthListResponse>{
            override fun onResponse(
                call: Call<YouthListResponse>,
                response: Response<YouthListResponse>
            ) {
                if(response.isSuccessful&&response.code()==200){
                    val yLT:YouthListResponse=response.body()!!
                    progressActiviity.showContent()
                    if(yLT!=null&&yLT.youthList.size>0) {
                        youthList.addAll(yLT.youthList)
                        adapter.notifyDataSetChanged()
                    }

                }
                else{
                    progressActiviity.showContent()
                    Toast.makeText(self,"Something went wrong,please try again...",Toast.LENGTH_SHORT ).show()
                }
            }

            override fun onFailure(call: Call<YouthListResponse>, t: Throwable) {
                progressActiviity.showContent()
                Log.d("error", t.message.toString())
            }
        })

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ChatFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}