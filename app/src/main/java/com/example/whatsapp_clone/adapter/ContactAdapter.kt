package com.example.whatsapp_clone.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp_clone.model.Contacts

class ContactAdapter(val data: ArrayList<Contacts>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private class MyViewHolder (val binding: )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = data.size
}