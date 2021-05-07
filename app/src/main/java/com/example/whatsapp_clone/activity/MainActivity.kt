package com.example.whatsapp_clone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.adapter.ViewPagerAdapter
import com.example.whatsapp_clone.databinding.ActivityMainBinding
import com.example.whatsapp_clone.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = "Whatsapp Clone"

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = 0
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
}