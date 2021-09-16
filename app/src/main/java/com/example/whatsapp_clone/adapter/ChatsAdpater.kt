package com.example.whatsapp_clone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.databinding.ItemChatsBinding
import java.lang.reflect.Array

class ChatsAdpater(val data: ArrayList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private class MyViewHolder(val binding: ItemChatsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]

        if (holder is MyViewHolder) {
            holder.binding.ciChats.setImageResource(R.mipmap.ic_launcher)
            holder.binding.tvNameChat.text = model
        }
    }

    override fun getItemCount(): Int = data.size

    fun chatUpdate(updateChats: ArrayList<String>) {
        data.clear()
        data.addAll(updateChats)
    }
}