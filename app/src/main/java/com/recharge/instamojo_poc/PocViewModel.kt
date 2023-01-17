package com.recharge.instamojo_poc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recharge.instamojo_poc.api.OrderCreatedResponse

class PocViewModel:ViewModel() {
    var responseLiveData = MutableLiveData<OrderCreatedResponse>()

    fun setLiveData(data:OrderCreatedResponse){
        Log.e("data loggrd",data.toString())
       responseLiveData.postValue(data)
    }
}