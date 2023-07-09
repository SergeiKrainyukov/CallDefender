package com.example.calldefender.ui.fragment.callsFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calldefender.R

class ViewPagerAdapter(private val data: List<String>) : RecyclerView.Adapter<ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(text: String) {
        itemView.findViewById<TextView>(R.id.text).text = text
    }
}