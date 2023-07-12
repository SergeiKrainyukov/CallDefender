package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallStatus
import com.example.calldefender.ui.model.CallUi

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

    private fun calculateCallStatusColor(callStatus: CallStatus): Int {
        return when (callStatus) {
            CallStatus.ACCEPTED -> R.color.accepted_call_color
            CallStatus.REJECTED -> R.color.rejected_call_color
        }
    }
}