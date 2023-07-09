package com.example.calldefender.ui.fragment.callsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calldefender.databinding.FragmentCallsBinding
import com.google.android.material.tabs.TabLayoutMediator

class CallsFragment : Fragment() {

    private lateinit var binding: FragmentCallsBinding

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

        initTabs()
    }

    private fun initTabs() {
        val data = listOf("Все", "Заблокированные")
        val adapter = ViewPagerAdapter(data)
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = data[position]
            }.attach()
        }
    }

}