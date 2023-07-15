package com.example.calldefender.ui.fragment.callsFragment.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R
import com.example.calldefender.ui.model.CallType
import com.example.calldefender.ui.model.CallUi

class CallUiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(callUi: CallUi) {
        itemView.findViewById<TextView>(R.id.number_tv).text = callUi.callNumber
        itemView.findViewById<TextView>(R.id.date_tv).text = callUi.callDate
        itemView.findViewById<View>(R.id.call_status_indicator).setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                calculateCallStatusColor(callUi.callType)
            )
        )
    }

    private fun calculateCallStatusColor(callType: CallType): Int {
        return when (callType) {
            CallType.ACCEPTED -> R.color.accepted_call_color
            CallType.REJECTED -> R.color.rejected_call_color
            else -> R.color.accepted_call_color
        }
    }
}