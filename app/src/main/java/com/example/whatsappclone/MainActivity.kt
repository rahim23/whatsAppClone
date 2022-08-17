package com.example.whatsappclone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2          : ViewPager2
    private lateinit var tabLayout           : TabLayout
    private lateinit var toolbar             : androidx.appcompat.widget.Toolbar
    private lateinit var appPagerAdapter     : AppPagerAdapter
    private lateinit var auth                :  FirebaseAuth
    private val titles                       = arrayListOf("Chats", "Status", "Calls")
    private lateinit var showContacts        : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar      = findViewById(R.id.toolbarMain)
        tabLayout    = findViewById(R.id.tabayoutMain)
        showContacts = findViewById(R.id.btContacts)
        viewPager2   = findViewById(R.id.viewPager2Main)
        auth         = FirebaseAuth.getInstance()



        toolbar.title = "whatsappClone"
        setSupportActionBar(toolbar)
        appPagerAdapter = AppPagerAdapter(this)
        viewPager2.adapter = appPagerAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = titles[position]

        }.attach()


        showContacts.setOnClickListener {

            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("OptionName", "friends")


            startActivity(intent)
        }

    }
        class AppPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> Chats()
                    1 -> Status()
                    2 -> Calls()

                    else -> Chats()
                }
            }


        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.Profile -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("OptionName", "Profile")
                    startActivity(intent)
                }
                R.id.About -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("OptionName", "about")
                    startActivity(intent)
                }

                R.id.searchContacts -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.putExtra("OptionName", "search")
                    startActivity(intent)
                }


                R.id.Logout -> {
                    auth.signOut()
                    val intent = Intent(this, AuthenticationActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
            return super.onOptionsItemSelected(item)
        }


}


