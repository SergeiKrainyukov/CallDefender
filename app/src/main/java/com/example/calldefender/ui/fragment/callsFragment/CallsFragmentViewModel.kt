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

    private val _callsLiveData = MutableLiveData<CallsFragmentViewPagerAdapterData>()
    val callsLiveData: LiveData<CallsFragmentViewPagerAdapterData>
        get() = _callsLiveData

    fun init() {
        disposables.add(
            callsRepository.getCalls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) return@subscribe
                    _callsLiveData.value = prepareCallsFragmentViewPagerAdapterData(callEntities)
                }, {})
        )
    }

    fun getCalls() {
        disposables.add(
            callsRepository.getCalls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) return@subscribe
                    _callsLiveData.value = prepareCallsFragmentViewPagerAdapterData(callEntities)
                }, {})
        )
    }

    private fun prepareCallsFragmentViewPagerAdapterData(callEntities: List<CallUi>): CallsFragmentViewPagerAdapterData {
        val callsFragmentViewPagerAdapterData = CallsFragmentViewPagerAdapterData()
        CallType.values().forEach { callType ->
            val callsFilteredByType = callEntities.filter { callUi ->
                if (callType == CallType.ALL) true else callUi.callType == callType
            }
            callsFragmentViewPagerAdapterData.update(callsFilteredByType, callType)
        }
        return callsFragmentViewPagerAdapterData
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}