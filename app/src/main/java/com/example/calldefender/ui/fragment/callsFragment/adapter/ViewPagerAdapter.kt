package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi

class ViewPagerAdapter(private val callsData: List<CallTypeData>) :
    RecyclerView.Adapter<ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        )
    }

    override fun getItemCount() = CallType.values().size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        if (position < callsData.size)
            holder.bind(callsData[position])
    }
}

data class CallTypeData(
    val callType: String,
    val calls: List<CallUi>
)