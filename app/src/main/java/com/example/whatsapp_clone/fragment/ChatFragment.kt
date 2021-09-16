package com.example.whatsapp_clone.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.adapter.ChatsAdpater
import com.example.whatsapp_clone.databinding.FragmentChatBinding
import com.example.whatsapp_clone.model.Chat
import com.example.whatsapp_clone.util.DATA_USERS
import com.example.whatsapp_clone.util.DATA_USER_CHATS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentChatBinding
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var thisContext: Context
    private var chatAdapter: ChatsAdpater? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        thisContext = container!!.context
        chatAdapter = ChatsAdpater(arrayListOf(), thisContext)
        binding = FragmentChatBinding.inflate(inflater, container, false)

        firebaseDB.collection(DATA_USERS).document(userId!!).addSnapshotListener {it, firebaseException ->
            if (firebaseException == null) {
                refreshChat()
            }
        }


        return binding.root
    }

    private fun refreshChat() {
        firebaseDB.collection(DATA_USERS)
            .document(userId!!)
            .get()
            .addOnSuccessListener {
                if (it.contains(DATA_USER_CHATS)) {
                    val partners = it[DATA_USER_CHATS]
                    val chat = arrayListOf<String>()
                    for (partner in (partners as HashMap<String, String>).keys) {
                        if (partners[partner] != null) {
                            chat.add(partners[partner]!!)
                        }
                    }
                    chatAdapter?.chatUpdate(chat)
                }

                binding.rvChats.layoutManager = LinearLayoutManager(activity)
                binding.rvChats.adapter = chatAdapter

            }
    }

    fun newChat (partnerID: String) {
        firebaseDB.collection(DATA_USERS)
            .document(userId!!)
            .get()
            .addOnSuccessListener {
                val userChatPartners = hashMapOf<String, String>()
                if (it[DATA_USER_CHATS] != null && it[DATA_USER_CHATS] is HashMap<*, *>) {
                    val userDocumentMap = it[DATA_USER_CHATS] as HashMap<String, String>
                    if (userDocumentMap.containsKey(partnerID)) {
                        return@addOnSuccessListener
                    } else {
                        userChatPartners.putAll(userDocumentMap)
                    }
                }

                firebaseDB.collection(DATA_USERS)
                    .document(partnerID)
                    .get()
                    .addOnSuccessListener { partnerDocument ->
                        val partnerChatPartners = hashMapOf<String, String>()
                        if (partnerDocument[DATA_USER_CHATS] != null && partnerDocument[DATA_USER_CHATS] is HashMap<*, *>) {
                            val partnerDocumentMap = partnerDocument[DATA_USER_CHATS] as HashMap<String, String>
                            partnerChatPartners.putAll(partnerDocumentMap)
                        }

                        val chatParticipant = arrayListOf(userId, partnerID)
                        val chat = Chat(chatParticipant)
                        val chatRef = firebaseDB.collection(DATA_USER_CHATS).document()
                        val userRef = firebaseDB.collection(DATA_USERS).document(userId)
                        val partnerRef = firebaseDB.collection(DATA_USERS).document(partnerID)

                        userChatPartners[partnerID] = chatRef.id
                        partnerChatPartners[userId] = chatRef.id

                        val batch = firebaseDB.batch()
                        batch.set(chatRef, chat)
                        batch.update(userRef, DATA_USER_CHATS, userChatPartners)
                        batch.update(partnerRef, DATA_USER_CHATS, partnerChatPartners)
                        batch.commit()
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}