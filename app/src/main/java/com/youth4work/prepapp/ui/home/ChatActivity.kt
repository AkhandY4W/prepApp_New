package com.youth4work.prepapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener
import com.squareup.picasso.Picasso
import com.youth4work.prepapp.databinding.ActivityChatBinding
import com.youth4work.prepapp.network.model.response.SubCatDetailsItem
import com.youth4work.prepapp.network.model.response.Youth
import com.youth4work.prepapp.network.model.response.YouthListResponse
import com.youth4work.prepapp.ui.adapter.ChatUserListingAdapter
import com.youth4work.prepapp.ui.base.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : BaseActivity() {
    lateinit var layout:ActivityChatBinding
    lateinit var subCatDetailsItem:SubCatDetailsItem
    lateinit var youthList: ArrayList<Youth>
    lateinit var adapter:ChatUserListingAdapter
    lateinit var llm:LinearLayoutManager
    var page:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout= ActivityChatBinding.inflate(layoutInflater)
        setContentView(layout.root)
        supportActionBar?.hide()
        llm= LinearLayoutManager(this)
        click_event()
        getUserList("nodefaultimage")
        getCategoryData()
    }

    override fun onTransactionSuccess() {
        TODO("Not yet implemented")
    }

    override fun onTransactionSubmitted() {
        TODO("Not yet implemented")
    }

    override fun onTransactionFailed() {
        TODO("Not yet implemented")
    }

    override fun onAppNotFound() {
        TODO("Not yet implemented")
    }

    private fun getUserList(flag:String) {
        val call=prepService.getYouthList(mUserManager.category.catid, mUserManager.user.userId,1,20,flag)
        call.enqueue(object :Callback<YouthListResponse>{
            override fun onResponse(
                call: Call<YouthListResponse>,
                response: Response<YouthListResponse>
            ) {
                if(response.isSuccessful&&response.code()==200){
                   val youthListResponse= response.body()!!
                    youthList= youthListResponse.youthList as ArrayList<Youth>
                    if(youthList.size>5) {
                        Picasso.get()
                            .load(youthList.get(0).imageURL).into(layout.imgUser)
                        Picasso.get()
                            .load(youthList.get(1).imageURL).into(layout.imgUser1)
                        Picasso.get()
                            .load(youthList.get(2).imageURL).into(layout.imgUser2)
                        Picasso.get()
                            .load(youthList.get(3).imageURL).into(layout.imgUser3)
                        Picasso.get()
                            .load(youthList.get(4).imageURL).into(layout.imgUser4)
                    }
                    layout.progressActiviity.showContent()
                    initalizedAdapter(youthListResponse)
                }
                else{
                    layout.progressActiviity.showContent()
                    Toast.makeText(self,"Something went wrong,please try again...",Toast.LENGTH_SHORT ).show()
                }
            }

            override fun onFailure(call: Call<YouthListResponse>, t: Throwable) {
                layout.progressActiviity.showContent()
                Log.d("error", t.message.toString())
            }
        })
        layout.recyclerChatUser.addOnScrollListener(object :EndlessRecyclerOnScrollListener(llm){
            override fun onLoadMore(currentPage: Int) {
               getUserList(page++)
            }
        })
    }

    fun getUserList(page:Int){
        val call=prepService.getYouthList(mUserManager.category.catid, mUserManager.user.userId,page,20,"")
        call.enqueue(object :Callback<YouthListResponse>{
            override fun onResponse(
                call: Call<YouthListResponse>,
                response: Response<YouthListResponse>
            ) {
                if(response.isSuccessful&&response.code()==200){
                   val yLT:YouthListResponse=response.body()!!
                    layout.progressActiviity.showContent()
                    if(yLT!=null&&yLT.youthList.size>0) {
                        youthList.addAll(yLT.youthList)
                        adapter.notifyDataSetChanged()
                    }

                }
                else{
                    layout.progressActiviity.showContent()
                    Toast.makeText(self,"Something went wrong,please try again...",Toast.LENGTH_SHORT ).show()
                }
            }

            override fun onFailure(call: Call<YouthListResponse>, t: Throwable) {
                layout.progressActiviity.showContent()
                Log.d("error", t.message.toString())
            }
        })
    }
    private fun initalizedAdapter(youthListResponse: YouthListResponse) {
        layout.recyclerChatUser.layoutManager=llm
        adapter= ChatUserListingAdapter(this, youthListResponse.youthList as ArrayList<Youth>)
        layout.recyclerChatUser.adapter=adapter
        adapter.notifyDataSetChanged()
    }

    private fun getCategoryData() {
        layout.progressActiviity.showLoading()
        if(mUserManager.category!=null){
          //  layout.progressActiviity.showContent()
            Picasso.get().load(mUserManager.category.subCategoryImg)
                .into(layout.imgSubCat)
            layout.txtPartentCat.text = mUserManager.category.parentCategory
            layout.txtSubCat.text = mUserManager.category.category
            layout.txtSubCatAttempt.text =
                "+" + mUserManager.category.attempts + " Students Attempted"
        }
        else {
            val cal = prepService.getSubCatDetails(mUserManager.category.catid)
            cal.enqueue(object : Callback<SubCatDetailsItem> {
                override fun onResponse(
                    call: Call<SubCatDetailsItem>,
                    response: Response<SubCatDetailsItem>
                ) {
                    if (response.isSuccessful && response.code() == 200) {
                //        layout.progressActiviity.showContent()
                        subCatDetailsItem = response.body()!!
                        Picasso.get().load(subCatDetailsItem?.subCategoryImg)
                            .into(layout.imgSubCat)
                        layout.txtPartentCat.text = subCatDetailsItem?.parentCategory
                        layout.txtSubCat.text = subCatDetailsItem?.subCategory
                        layout.txtSubCatAttempt.text =
                            "+" + subCatDetailsItem?.aspirants + " Students Attempted"
                    } else {
                     //   layout.progressActiviity.showContent()
                        Toast.makeText(
                            self,
                            "Something went wrong,please try again...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SubCatDetailsItem>, t: Throwable) {
                    //layout.progressActiviity.showContent()
                    Log.d("error", t.message.toString())
                }
            })
        }
    }

    private fun click_event() {
       layout.txtChat.setOnClickListener {
           layout.cardCat.visibility= View.GONE
           layout.recyclerChatUser.visibility=View.VISIBLE
           layout.progressActiviity.showLoading()
           layout.txtTitle.text="Student attempted for"
           layout.txtSubCatName.text=  mUserManager.category.category
           getUserList(1)
       }
        layout.btnBack.setOnClickListener {
            finish()
        }
    }
}