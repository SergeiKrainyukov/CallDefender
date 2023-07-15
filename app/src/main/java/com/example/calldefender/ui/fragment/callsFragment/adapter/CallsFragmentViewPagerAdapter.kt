package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi

class CallsFragmentViewPagerAdapter(private var callsFragmentViewPagerAdapterData: CallsFragmentViewPagerAdapterData) :
    RecyclerView.Adapter<ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        )

    override fun getItemCount() = CallType.values().size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(callsFragmentViewPagerAdapterData.items()[position])
    }

    fun updateTabData(callUiList: List<CallUi>, callType: CallType) {
        callsFragmentViewPagerAdapterData.update(callUiList, callType)
        notifyItemChanged(callType.ordinal)
    }

    fun setData(callsFragmentViewPagerAdapterData: CallsFragmentViewPagerAdapterData) {
        this.callsFragmentViewPagerAdapterData = callsFragmentViewPagerAdapterData
        notifyDataSetChanged()
    }
}

class CallsFragmentViewPagerAdapterData {
    private val items: List<MutableList<CallUi>> = List(CallType.values().size) { mutableListOf() }

    fun items(): List<List<CallUi>> = items

    fun update(callUiList: List<CallUi>, callType: CallType) {
        val typeIndex = callType.ordinal
        items[typeIndex].clear()
        items[typeIndex].addAll(callUiList)
    }
}

