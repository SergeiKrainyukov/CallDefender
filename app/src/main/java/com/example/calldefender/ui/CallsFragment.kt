package com.example.calldefender.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.calldefender.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CallsFragment : Fragment(R.layout.fragment_calls) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
    }

    private fun initTabs() {
        val data = listOf("Все", "Заблокированные")
        val adapter = ViewPagerAdapter(data)
        val viewPager = requireView().findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = data[position]
        }.attach()
    }

}