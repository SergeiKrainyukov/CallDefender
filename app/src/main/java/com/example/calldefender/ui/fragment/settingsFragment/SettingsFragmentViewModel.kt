package com.example.calldefender.ui.fragment.settingsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calldefender.repository.SettingsRepository
import com.example.calldefender.ui.model.SettingUI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SettingsFragmentViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val settingsLiveData = MutableLiveData<List<SettingUI>>()
    fun settingsLiveData(): LiveData<List<SettingUI>> = settingsLiveData

    private val disposables = CompositeDisposable()

    fun init() {
        disposables.add(
            settingsRepository.getAllSettings().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    settingsLiveData.value = it
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun updateSetting(settingUI: SettingUI) {
        disposables.add(
            settingsRepository.update(settingUI).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({}, {})
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}