package com.example.whatsapp_clone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.whatsapp_clone.databinding.ItemChatsBinding
import com.example.whatsapp_clone.model.User
import com.example.whatsapp_clone.util.DATA_CHATS_PARTICIPANTS
import com.example.whatsapp_clone.util.DATA_USERS
import com.example.whatsapp_clone.util.DATA_USER_CHATS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatsAdpater(val data: ArrayList<String>, val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val firebaseDb = FirebaseFirestore.getInstance()
    var partnerID: String? = null
    var imageUrl: String? = null

    private class MyViewHolder(val binding: ItemChatsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]

        if (holder is MyViewHolder) {
            firebaseDb.collection(DATA_USER_CHATS)
                .document(model)
                .get()
                .addOnSuccessListener {
                    val chatParticipants = it[DATA_CHATS_PARTICIPANTS]
                    if (chatParticipants != null) {
                        for (participants in chatParticipants as ArrayList<String>) {
                            if (participants != userId) {
                                partnerID = participants
                                firebaseDb.collection(DATA_USERS)
                                    .document(partnerID!!)
                                    .get()
                                    .addOnSuccessListener {
                                        val user = it.toObject(User::class.java)
                                        imageUrl = user?.imageUrl
                                        holder.binding.tvNameChat.text = user?.name
                                        if (imageUrl != null) {
                                            userImage(context, user?.imageUrl!!, holder.binding.ciChats)
                                        }
                                    }
                                    .addOnFailureListener {
                                        it.printStackTrace()
                                    }
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    override fun getItemCount(): Int = data.size

    fun chatUpdate(updateChats: ArrayList<String>) {
        data.clear()
        data.addAll(updateChats)
    }

    private fun userImage(context: Context, uri: String, imageView: ImageView) {
        val option = RequestOptions()
        Glide.with(context)
            .load(uri)
            .apply(option)
            .into(imageView)
    }
}