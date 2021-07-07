package com.example.whatsapp_clone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp_clone.fragment.ChatFragment 
import com.example.whatsapp_clone.fragment.StatusFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    val chatFragment = ChatFragment()
    val statusFragment = StatusFragment()

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            1 -> return chatFragment
            2 -> return statusFragment
        }
        return chatFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            1 -> return "Chat"
            2 -> return "Status"
        }
        return null
    }

}