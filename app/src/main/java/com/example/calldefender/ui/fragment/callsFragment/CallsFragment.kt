package com.example.calldefender.ui.fragment.callsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.databinding.FragmentCallsBinding
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallsFragmentViewPagerAdapter
import com.example.calldefender.ui.fragment.callsFragment.adapter.CallsFragmentViewPagerAdapterData
import com.example.calldefender.ui.model.CallType
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class CallsFragment : Fragment() {

    @Inject
    lateinit var viewModel: CallsFragmentViewModel
    private lateinit var binding: FragmentCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as CallDefenderApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        initTabs()
        viewModel.init()
    }

    private fun bindViewModel() {
        viewModel.callsDataLiveData().observe(viewLifecycleOwner) {
            (binding.viewPager.adapter as CallsFragmentViewPagerAdapter).setData(it)
        }
    }

    private fun initTabs() {
        val adapter = CallsFragmentViewPagerAdapter(CallsFragmentViewPagerAdapterData())
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setCustomView(R.layout.item_tab)
                (tab.customView as TextView).text = CallType.values()[position].callStatusName
            }.attach()
        }
    }
}