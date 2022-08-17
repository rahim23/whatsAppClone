package com.example.whatsappclone

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Contacts : Fragment() {
    private lateinit var contactsRecyclerView : RecyclerView
    private lateinit var contactLayoutManager : RecyclerView.LayoutManager
    private lateinit var contactsAdapter      : ContactsAdapter
    private val contactInfo                   = arrayListOf<User>()
    private lateinit var fstore               : FirebaseFirestore
    private lateinit var auth                 : FirebaseAuth
  //  private lateinit var chatRommID              : String
   // private lateinit var friendsID              : String
    private lateinit var userId              : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view                           = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsRecyclerView               = view.findViewById(R.id.contactsRecyclerView)
        contactLayoutManager               = LinearLayoutManager(context as Activity)
        auth                               = FirebaseAuth.getInstance()
        userId                             = auth.currentUser!!.uid
        fstore                             = FirebaseFirestore.getInstance()
        fstore.collection("users").document(userId).collection("friends").get().addOnSuccessListener {
            if(!it.isEmpty)
            {
                contactInfo.clear()
                val ListContact            = it.documents
                for(i in ListContact)
                {

                        val friendsID = i.id
                      //  val chatRommID = i.getString("chatroomid")

                    val chatRommID = i.getString("chatRoom")
                    fstore.collection("users").document(friendsID).addSnapshotListener { value, error ->
                            if(error!=null)
                            {
                                Log.d("","")
                            }
                            else
                            {
                                val contact                        = User(
                                    friendsID,
                                    value!!.getString("userName").toString(),
                                    value.getString("userEmail").toString(),
                                    value.getString("userStatus").toString(),
                                    "https://upload.wikimedia.org/wikipedia/commons/1/18/Manchester_United_v_Newcastle_United%2C_11_September_2021_%2810%29.jpg",
                                    chatRommID.toString()
                                )
                                contactInfo.add(contact)
                                contactsAdapter                    = ContactsAdapter(context as Context,contactInfo)
                                contactsRecyclerView.adapter       = contactsAdapter
                                contactsRecyclerView.layoutManager = contactLayoutManager
                                contactsRecyclerView.addItemDecoration(
                                    DividerItemDecoration(
                                        contactsRecyclerView.context,
                                        (contactLayoutManager as LinearLayoutManager).orientation

                                    )


                                )



                            }



                        }




                    }

                        }

            }

     //   }

        return view
    }

}