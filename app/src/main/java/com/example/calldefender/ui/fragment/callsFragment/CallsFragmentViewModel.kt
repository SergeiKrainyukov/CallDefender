package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calldefender.data.CallEntityDao
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallTypeData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CallsFragmentViewModel @Inject constructor(
    private val callEntityDao: CallEntityDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val callsDataLiveData = MutableLiveData<List<CallTypeData>>()
    fun callsDataLiveData(): LiveData<List<CallTypeData>> = callsDataLiveData

    fun init() {
        disposables.add(
            callEntityDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ callEntities ->
                    if (callEntities.isEmpty()) return@subscribe
                    val data = mutableListOf<CallTypeData>()
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