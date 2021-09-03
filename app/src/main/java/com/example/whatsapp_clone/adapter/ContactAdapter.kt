package com.example.whatsapp_clone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp_clone.databinding.ItemContactBinding
import com.example.whatsapp_clone.model.Contacts

class ContactAdapter(val data: ArrayList<Contacts>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private class MyViewHolder (val binding: ItemContactBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]

        if (holder is MyViewHolder) {
            holder.binding.tvName.text = model.name
            holder.binding.tvNumber.text = model.phone
            holder.binding.llContact.setOnClickListener {
                onClickListener!!.onClick(position, model)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    // code for click listener
    private var onClickListener: OnClickListener? = null

    fun setOnClickListener (onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick (position: Int, model: Contacts)
    }
}