package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallUi

class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(calls: List<CallUi>) {
        val adapter = CallsAdapter(calls)
        itemView.findViewById<RecyclerView>(R.id.calls_rv).adapter = adapter
    }
}