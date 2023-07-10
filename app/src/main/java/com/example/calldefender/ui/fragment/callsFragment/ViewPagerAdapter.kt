package com.example.calldefender.ui.fragment.callsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R

class ViewPagerAdapter(private val calls: List<List<CallUi>>) :
    RecyclerView.Adapter<ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return calls.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(calls[position])
    }
}

class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(calls: List<CallUi>) {
        val adapter = CallsAdapter(calls)
        itemView.findViewById<RecyclerView>(R.id.calls_rv).adapter = adapter
    }
}

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

data class CallUi(val callNumber: String, val callDate: String, val callStatus: CallStatus)

class CallUiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(callUi: CallUi) {
        itemView.findViewById<TextView>(R.id.number_tv).text = callUi.callNumber
        itemView.findViewById<TextView>(R.id.date_tv).text = callUi.callDate
        itemView.findViewById<View>(R.id.call_status_indicator).setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                calculateCallStatusColor(callUi.callStatus)
            )
        )
    }
}

private fun calculateCallStatusColor(callStatus: CallStatus): Int {
    return when (callStatus) {
        CallStatus.ACCEPTED -> R.color.accepted_call_color
        CallStatus.REJECTED -> R.color.rejected_call_color
    }
}

enum class CallStatus {
    ACCEPTED,
    REJECTED
}