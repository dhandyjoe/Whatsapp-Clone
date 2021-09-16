package com.example.whatsapp_clone.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsapp_clone.adapter.ContactAdapter
import com.example.whatsapp_clone.databinding.ActivityContactBinding
import com.example.whatsapp_clone.fragment.ChatFragment
import com.example.whatsapp_clone.model.Contacts
import com.example.whatsapp_clone.util.CONTACTS
import com.example.whatsapp_clone.util.DATA_USERS
import com.example.whatsapp_clone.util.DATA_USER_PHONE
import com.google.firebase.firestore.FirebaseFirestore

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private val contactList = ArrayList<Contacts>()
    private val chatsFragment = ChatFragment()
    private val firebaseDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.title = "Whatsapp Clone"

        getContact()
    }

    private fun getContact() {
        binding.llProgressBar.visibility = View.VISIBLE
        contactList.clear()
        val newList = ArrayList<Contacts>()
        val phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)

        while (phone!!.moveToNext()) {
            val name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            newList.add(Contacts(name, phoneNumber))
        }
        contactList.addAll(newList)
        phone.close()

        setupList()
    }

    fun setupList() {
        binding.llProgressBar.visibility = View.GONE
        binding.rvContacts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val data = ContactAdapter(contactList)
        binding.rvContacts.adapter = data

        data.setOnClickListener(object : ContactAdapter.OnClickListener {
            override fun onClick(position: Int, model: Contacts) {
                checkNewChatUser(model.name.toString(), model.phone.toString())
                Log.d("aan", model.phone.toString())
            }
        })
    }

    private fun checkNewChatUser(name: String, phone:String) {
        if (!name.isNullOrEmpty() && !phone.isNullOrEmpty()) {
            firebaseDB.collection(DATA_USERS)
                .whereEqualTo(DATA_USER_PHONE, phone)
                .get()
                .addOnSuccessListener {
                    if (it.documents.size > 0) {
                        chatsFragment.newChat(it.documents[0].id)
                        onBackPressed()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("User not found")
                            .setMessage("$name does not have an account. Send them an SMS to install this app!")
                            .setPositiveButton("OK") { _,_ ->
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("sms:$phone")
                                intent.putExtra("sms_body", "Hi. I'm using this new cool WhatsClone app. You should install it too so we can chat there")
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
                .addOnFailureListener {e ->
                    Toast.makeText(this, "An error occured. Please try again later", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }

}