package com.example.calldefender.ui.fragment.callsFragment

import androidx.lifecycle.ViewModel
import com.example.calldefender.data.CallEntity
import java.util.Date
import javax.inject.Inject

class CallsFragmentViewModel @Inject constructor() : ViewModel() {
    val entity = CallEntity(0, "+79991234567", Date().time, false)
}