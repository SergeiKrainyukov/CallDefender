package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calldefender.data.CallEntityDao
import com.example.calldefender.ui.model.CallStatus
import com.example.calldefender.ui.model.CallUi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

class CallsFragmentViewModel @Inject constructor(
    private val callEntityDao: CallEntityDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val callsDataLiveData = MutableLiveData<List<List<CallUi>>>()
    fun callsDataLiveData(): LiveData<List<List<CallUi>>> = callsDataLiveData

    fun init() {
        disposables.add(
            callEntityDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    val data = mutableListOf(mutableListOf(), mutableListOf<CallUi>())
                    if (callEntities.isEmpty()) {
                        data[0].add(
                            CallUi(
                                callNumber = "+79611877192",
                                callDate = Date().formatToPattern(DatePatterns.DEFAULT.pattern),
                                callStatus = CallStatus.ACCEPTED
                            )
                        )

                    } else {
                        callEntities.forEach {
                            data[0].add(
                                CallUi(
                                    callNumber = it.callNumber,
                                    callDate = Date(it.callDate).formatToPattern(DatePatterns.DEFAULT.pattern),
                                    callStatus = if (it.rejected) CallStatus.REJECTED else CallStatus.ACCEPTED
                                )
                            )
                        }
                    }
                    callsDataLiveData.value = data
                }, { error ->

                })
        )
    }



    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}