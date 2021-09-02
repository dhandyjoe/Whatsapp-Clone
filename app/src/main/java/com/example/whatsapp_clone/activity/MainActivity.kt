package com.example.whatsapp_clone.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Placeholder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.databinding.ActivityMainBinding
import com.example.whatsapp_clone.databinding.ActivityProfileBinding
import com.example.whatsapp_clone.databinding.FragmentChatBinding
import com.example.whatsapp_clone.fragment.ChatFragment
import com.example.whatsapp_clone.fragment.StatusFragment
import com.example.whatsapp_clone.fragment.StatusUpdateFragment
import com.example.whatsapp_clone.util.PERMISSION_REQUEST_CONTACTS
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var mSectionPageAdapter: SectionPageAdapter? = null

    private val chatsFragment = ChatFragment()
    private val statusFragment = StatusFragment()
    private val statusUpdateFragment = StatusUpdateFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mSectionPageAdapter = SectionPageAdapter(supportFragmentManager)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Whatsapp Clone"

        binding.viewPager.adapter = mSectionPageAdapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager))
        resizeTab()
        binding.tabs.getTabAt(1)?.select()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    0 -> {binding.fabMain.hide()}
                    1 -> {binding.fabMain.show()}
                    2 -> {binding.fabMain.hide()}
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


    }

    fun onNewChat() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder(this)
                    .setTitle("Contact Information")
                    .setMessage("This app requires access to your contacts to initiate a conversation.")
                    .setPositiveButton("Ask Me!") { _, _ -> reqeustContactPermission()}
                    .setNegativeButton("No") { _, _ -> }

            }
        } else {
            // Permission granted

        }
    }

    fun reqeustContactPermission () {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CONTACTS)
    }

    private fun resizeTab() {
        val layout = (binding.tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.4f
        layout.layoutParams = layoutParams
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> onLogout()
            R.id.action_profile -> startActivity(Intent(this, ProfileActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onLogout() {
        firebaseAuth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    inner class SectionPageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> statusUpdateFragment
                1 -> chatsFragment
                2 -> statusFragment
                else -> chatsFragment
            }
        }

    }
}