package com.example.whatsapp_clone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import com.example.whatsapp_clone.adapter.ContactAdapter
import com.example.whatsapp_clone.databinding.ActivityContactBinding
import com.example.whatsapp_clone.model.Contacts
import com.example.whatsapp_clone.util.CONTACTS

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    private val contactList = ArrayList<Contacts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val data = ContactAdapter(contactList)
        binding.rvContacts.adapter = data

        data.setOnClickListener(object : ContactAdapter.OnClickListener {
            override fun onClick(position: Int, model: Contacts) {
                val intent = Intent(this@ContactActivity, MainActivity::class.java)
                intent.putExtra(CONTACTS, model)
                startActivity(intent)
                finish()
            }

        })
    }
}