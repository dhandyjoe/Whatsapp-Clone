package com.example.whatsapp_clone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.Placeholder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp_clone.R
import com.example.whatsapp_clone.databinding.ActivityMainBinding
import com.example.whatsapp_clone.databinding.ActivityProfileBinding
import com.example.whatsapp_clone.databinding.FragmentChatBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var mSectionPageAdapter: SectionPageAdapter? = null

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
            return PlaceholderFragment.newIntent(position + 1)
        }

    }

    class PlaceholderFragment: Fragment() {
        private lateinit var binding: FragmentChatBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentChatBinding.inflate(inflater, container, false)
            binding.nameChat.text = "Hello world from section ${arguments?.getInt(ARG)}"
            return binding.root
        }

        companion object {
            private val ARG = "arg"

            fun newIntent(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}