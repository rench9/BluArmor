package com.r4hu7.bluarmor.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.r4hu7.bluarmor.R
import com.r4hu7.bluarmor.databinding.AdapterChatBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val items: ArrayList<Feed> = arrayListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(p0.context),
                R.layout.adapter_chat,
                p0,
                false
            )
        )
    }

    fun addItem(feed: Feed) {
        items.add(feed)
        notifyItemInserted(items.size - 1)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.binding.msg = items[p1].msg
        viewHolder.binding.imgId = items[p1].profileImage
        viewHolder.binding.isClient = items[p1].isClient
    }

    class ViewHolder(val binding: AdapterChatBinding) : RecyclerView.ViewHolder(binding.root)

    class Feed(val msg: String, val profileImage: Int, val isClient: Boolean)
}