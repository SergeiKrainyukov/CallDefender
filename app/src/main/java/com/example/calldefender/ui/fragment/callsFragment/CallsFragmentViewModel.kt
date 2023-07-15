package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calldefender.common.DatePatterns
import com.example.calldefender.common.formatToPattern
import com.example.calldefender.data.CallEntity
import com.example.calldefender.data.CallEntityDao
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallsFragmentViewPagerAdapterData
import com.example.calldefender.ui.model.CallType
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

    private val callsDataLiveData = MutableLiveData<CallsFragmentViewPagerAdapterData>()
    fun callsDataLiveData(): LiveData<CallsFragmentViewPagerAdapterData> = callsDataLiveData

    fun init() {
        disposables.add(
            callEntityDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) {
                        generateCalls()
                        return@subscribe
                    }
                    val callsFragmentViewPagerAdapterData = CallsFragmentViewPagerAdapterData()
                    CallType.values().forEach { callType ->
                        val callsFilteredByType = callEntities.filter { callEntity ->
                            if (callType == CallType.ALL) true else callEntity.callType == callType.ordinal
                        }.map { callEntity ->
                            CallUi(
                                callEntity.callNumber,
                                Date(callEntity.callDate).formatToPattern(DatePatterns.DEFAULT.pattern),
                                CallType.values()[callEntity.callType]
                            )
                        }
                        callsFragmentViewPagerAdapterData.update(callsFilteredByType, callType)
                    }
                    callsDataLiveData.value = callsFragmentViewPagerAdapterData
                }, {})
        )
    }

    private fun generateCalls() {
        disposables.add(
            callEntityDao.insert(
                CallEntity(
                    0,
                    "+79611877192",
                    Date().time,
                    CallType.ACCEPTED.ordinal
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        disposables.add(
            callEntityDao.insert(
                CallEntity(
                    1,
                    "+79621878193",
                    Date().time,
                    CallType.REJECTED.ordinal
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}