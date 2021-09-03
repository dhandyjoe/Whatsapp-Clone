package com.example.whatsapp_clone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.example.whatsapp_clone.databinding.ActivityContactBinding
import com.example.whatsapp_clone.model.Contacts

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
    }
}