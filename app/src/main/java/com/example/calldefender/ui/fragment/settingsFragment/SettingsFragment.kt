package com.example.calldefender.ui.fragment.settingsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.databinding.FragmentSettingsBinding
import com.example.calldefender.ui.model.SettingUI
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    @Inject
    lateinit var viewModel: SettingsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
        viewModel.init()
    }

    private fun initViews() {
        binding.blockUnfamiliarCallsSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.updateSetting(
                SettingUI.BlockUnfamiliarCallsSettingUI(
                    getString(R.string.block_unfamiliar_calls_setting),
                    checked
                )
            )
        }
    }

    private fun bindViewModel() {
        viewModel.settingsLiveData.observe(viewLifecycleOwner) {
            it.forEach {
                when (it){
                    is SettingUI.BlockUnfamiliarCallsSettingUI -> {
                        binding.blockUnfamiliarCallsSwitch.isChecked = it.isEnabled
                    }
                    else -> {}
                }
            }
        }
    }
}