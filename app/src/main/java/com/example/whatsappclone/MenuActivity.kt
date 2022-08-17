package com.example.whatsappclone

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MenuActivity :AppCompatActivity(),  SearchView.OnQueryTextListener  {
    private lateinit var toolbarMenu         : androidx.appcompat.widget.Toolbar
    private lateinit var frameLayout         : FrameLayout
    private lateinit var optionValue         : String
    private lateinit var queryTerm           : String
    private lateinit var searchRecyclerView  : RecyclerView
    private lateinit var searchAdapter       : SearchAdapter
    private lateinit var searchLayoutManager : RecyclerView.LayoutManager
    private var register                     : ListenerRegistration?=null
    private val searchInfo                      = arrayListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        toolbarMenu = findViewById(R.id.toolbarMenu)
        frameLayout = findViewById(R.id.frameLayout)



        if(intent!=null){
            optionValue = intent.getStringExtra("OptionName").toString()
            when(optionValue)
            {
                "Profile"->
                {
                  frameLayout.visibility= View.VISIBLE
                  supportFragmentManager.beginTransaction().replace(R.id.frameLayout,Profile()).commit()
                  toolbarMenu.title="Profile"
                }

                "about"->
                {
                    frameLayout.visibility= View.VISIBLE
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,About()).commit()
                    toolbarMenu.title="About Us"

                }
                "search" ->
                {
                    searchRecyclerView = findViewById(R.id.RecycleViewSearch)
                    searchLayoutManager = LinearLayoutManager(this)
                    searchRecyclerView.visibility=View.VISIBLE
                    toolbarMenu.title="Search Users"
                   setSupportActionBar(toolbarMenu)
                    searchRecyclerView.addItemDecoration(
                        DividerItemDecoration(
                            searchRecyclerView.context,
                            (searchLayoutManager as LinearLayoutManager).orientation))



                }

                "friends"->{
                    frameLayout.visibility= View.VISIBLE
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,Contacts()).commit()
                    toolbarMenu.title="friendsList"


                }
                "ChatMessaging"->
                {
                  frameLayout.visibility = View.VISIBLE
                  toolbarMenu.title      = intent.getStringExtra("receiverName")
                  val fragmentName       = Messaging()
                  val transaction        = supportFragmentManager.beginTransaction()
                  val bundle             = Bundle()
                  bundle.putString("documentID",intent.getStringExtra("chatRoom"))
                  bundle.putString("friendName",intent.getStringExtra("ReceiverName"))
                  fragmentName.arguments = bundle
                  transaction.replace(R.id.frameLayout,fragmentName).commit()
                }
                "contactMessaging"->
                {
                    frameLayout.visibility = View.VISIBLE
                    toolbarMenu.title      = intent.getStringExtra("friendName")
                    val fragmentName       = Messaging()
                    val transaction        = supportFragmentManager.beginTransaction()
                    val contactbundle      = Bundle()
                    contactbundle.putString("chatRoomID",intent.getStringExtra("chatRoom"))
                    contactbundle.putString("friendUID",intent.getStringExtra("friendUID"))
                    fragmentName.arguments = contactbundle
                    transaction.replace(R.id.frameLayout,fragmentName).commit()



                }


            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.searh,menu)
        val searchView  = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
          if(query!=null)
          {
              searchInfo.clear()
              queryTerm = query
              if(queryTerm.isNotEmpty()) {
                  searchUsers()
              }

          }

       return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText!=null)
        {
            queryTerm = newText
            if(queryTerm.isNotEmpty()) {
                searchUsers()
            }

        }

        return true
    }

    private fun searchUsers() {
      register = FirebaseFirestore.getInstance().collection("users")
     //   FirebaseFirestore.getInstance().collection("users")
            .orderBy("userName").startAt(queryTerm).limit(5).addSnapshotListener { snapshot,exception->
            if(exception!=null)
            {
                Log.e("onError","Some Error Occured")

            }
            else
            {
                if(!snapshot?.isEmpty!!)
                {
                    searchInfo.clear()
                   val searchList = snapshot.documents
                   for(i in searchList) {
                       if (FirebaseAuth.getInstance().currentUser!!.uid == i.id) {
                           Log.d("onSuccess","User running App")

                       } else {
                           val contact = User(
                               i.id,
                               i.getString("userName").toString(),
                               i.getString("userEmail").toString(),
                               i.getString("userStatus").toString(),
                               "https://upload.wikimedia.org/wikipedia/commons/1/18/Manchester_United_v_Newcastle_United%2C_11_September_2021_%2810%29.jpg",
                               "0"
                           )
                           searchInfo.add(contact)
                           searchAdapter = SearchAdapter(this, searchInfo)

                           searchRecyclerView.adapter = searchAdapter
                           searchRecyclerView.layoutManager = searchLayoutManager

                           searchRecyclerView.addItemDecoration(
                               DividerItemDecoration(
                                   searchRecyclerView.context,
                                   (searchLayoutManager as LinearLayoutManager).orientation
                               )
                           )


                       }
                   }


                }

                   }


                }






            }

    override fun onDestroy() {
        register?.remove()
        super.onDestroy()
    }


 }