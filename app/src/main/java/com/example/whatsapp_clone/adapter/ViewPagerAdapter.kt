package com.example.whatsapp_clone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp_clone.fragment.ChatFragment
import com.example.whatsapp_clone.fragment.ContactFragment
import com.example.whatsapp_clone.fragment.StatusFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    val chatFragment = ChatFragment()
    val contactFragment = ContactFragment()
    val statusFragment = StatusFragment()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return chatFragment
            1 -> return statusFragment
            2 -> return contactFragment
        }
        return chatFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Chat"
            1 -> return "Status"
            2 -> return "Contact"
        }
        return null
    }
}