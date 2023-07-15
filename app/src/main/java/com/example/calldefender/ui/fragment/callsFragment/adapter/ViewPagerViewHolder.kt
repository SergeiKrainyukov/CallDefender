package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R

class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(callTypeData: CallTypeData) {
        val adapter = CallsAdapter(callTypeData.calls)
        itemView.findViewById<RecyclerView>(R.id.calls_rv).adapter = adapter
    }
}