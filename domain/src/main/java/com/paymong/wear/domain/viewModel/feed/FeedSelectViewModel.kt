package com.paymong.wear.domain.viewModel.feed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymong.wear.domain.model.FeedModel
import com.paymong.wear.domain.repository.AppInfoRepository
import com.paymong.wear.domain.viewModel.code.FeedSelectCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedSelectViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository
) : ViewModel() {
    val processCode: LiveData<FeedSelectCode> get() = _processCode
    private val _processCode = MutableLiveData(FeedSelectCode.LOAD_FEED_LIST)

    var foodList: LiveData<List<FeedModel>> = MutableLiveData(ArrayList())
    var snackList: LiveData<List<FeedModel>> = MutableLiveData(ArrayList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            foodList = appInfoRepository.getFoodList() // MutableLiveData(fl)
            snackList = appInfoRepository.getSnackList() // MutableLiveData(sl)

            delay(300)
            _processCode.postValue(FeedSelectCode.STAND_BY)
        }
    }

    fun feeding(feedCode: String) {
        Log.d("FeedSelectViewModel", "Call - feeding($feedCode)")
    }
}