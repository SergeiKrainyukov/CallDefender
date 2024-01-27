package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calldefender.repository.CallsRepository
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallsFragmentViewPagerAdapterData
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallsFragmentViewModel @Inject constructor(
    private val callsRepository: CallsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _callsLiveData = MutableLiveData<CallsFragmentViewPagerAdapterData>()
    val callsLiveData: LiveData<CallsFragmentViewPagerAdapterData>
        get() = _callsLiveData

    fun getCalls() {
        viewModelScope.launch {
            _callsLiveData.value =
                prepareCallsFragmentViewPagerAdapterData(callsRepository.getCalls())
        }
    }

    private fun prepareCallsFragmentViewPagerAdapterData(callEntities: List<CallUi>): CallsFragmentViewPagerAdapterData {
        val callsFragmentViewPagerAdapterData = CallsFragmentViewPagerAdapterData()
        CallType.entries.forEach { callType ->
            val callsFilteredByType = callEntities.filter { callUi ->
                callType == CallType.ALL || callUi.callType == callType
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