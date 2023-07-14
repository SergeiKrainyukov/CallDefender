package com.example.calldefender.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class AppViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = viewModelProviders[modelClass]
        if (provider != null) return provider.get() as T
        else throw IllegalArgumentException("Incorrect ViewModel")
    }
}