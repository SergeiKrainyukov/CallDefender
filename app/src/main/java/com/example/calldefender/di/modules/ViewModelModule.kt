package com.example.calldefender.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calldefender.di.AppViewModelFactory
import com.example.calldefender.di.ViewModelKey
import com.example.calldefender.ui.fragment.callsFragment.CallsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CallsFragmentViewModel::class)
    internal abstract fun bindCallsFragmentViewModel(viewModel: CallsFragmentViewModel): ViewModel

}