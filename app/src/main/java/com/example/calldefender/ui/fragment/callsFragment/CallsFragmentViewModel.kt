package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallsFragmentViewPagerAdapterData
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CallsFragmentViewModel @Inject constructor(
    private val callsRepository: CallsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val callsDataLiveData = MutableLiveData<CallsFragmentViewPagerAdapterData>()
    fun callsDataLiveData(): LiveData<CallsFragmentViewPagerAdapterData> = callsDataLiveData

    fun init() {
        disposables.add(
            callsRepository.getCalls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) {
//                        generateCalls()
                        return@subscribe
                    }
                    val callsFragmentViewPagerAdapterData = CallsFragmentViewPagerAdapterData()
                    CallType.values().forEach { callType ->
                        val callsFilteredByType = callEntities.filter { callUi ->
                            if (callType == CallType.ALL) true else callUi.callType == callType
                        }
                        callsFragmentViewPagerAdapterData.update(callsFilteredByType, callType)
                    }
                    callsDataLiveData.value = callsFragmentViewPagerAdapterData
                }, {})
        )
    }

    private fun generateCalls() {
        disposables.add(
            callsRepository.addCall(
                CallUi(
                    "+79611877192",
                    "08.10.2022 14:11:28",
                    CallType.ACCEPTED
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        disposables.add(
            callsRepository.addCall(
                CallUi(
                    "+79621899194",
                    "08.12.2022 12:05:37",
                    CallType.REJECTED
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