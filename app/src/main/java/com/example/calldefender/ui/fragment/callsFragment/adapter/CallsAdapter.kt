package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallUi

class CallsAdapter(private val calls: List<CallUi>) : RecyclerView.Adapter<CallUiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallUiViewHolder {
        return CallUiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_call, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return calls.size
    }

    override fun onBindViewHolder(holder: CallUiViewHolder, position: Int) {
        holder.bind(calls[position])
    }

}