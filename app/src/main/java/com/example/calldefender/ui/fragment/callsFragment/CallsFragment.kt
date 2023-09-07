package com.example.calldefender.ui.fragment.callsFragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.calldefender.CallDefenderApp
import com.example.calldefender.R
import com.example.calldefender.common.UPDATE_CALLS_ACTION
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

    private val updateUIReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.getCalls()
        }
    }

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

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(updateUIReceiver, IntentFilter(UPDATE_CALLS_ACTION))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(updateUIReceiver)
    }

    private fun bindViewModel() {
        viewModel.callsLiveData.observe(viewLifecycleOwner) {
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