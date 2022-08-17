package com.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var appPagerAdapter: AuthenticationActivity.AppPagerAdapter
    private val titles= arrayListOf("Login","SignUp")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        tabLayout=findViewById(R.id.tabauth)
        viewPager2=findViewById(R.id.viewpagerauth)


        appPagerAdapter= AuthenticationActivity.AppPagerAdapter(this)
        viewPager2.adapter=appPagerAdapter
        TabLayoutMediator(tabLayout,viewPager2){
                tab,position->tab.text=titles[position]

        }.attach()

    }



    class AppPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0->Login()
                1->SignUp()

                else ->Login()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener (this)
        if(FirebaseAuth.getInstance().currentUser!=null )
        {
            StartMainActivity()
        }

    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
         if(p0.currentUser!=null)
         {
             StartMainActivity()

         }

    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener (this)

    }

  private fun StartMainActivity() {
      val intent = Intent(this,MainActivity::class.java)
      startActivity(intent)

  }



}








