package com.example.whatsappclone

import android.app.Activity
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
import com.google.firebase.firestore.Query

class Chats : Fragment() {
    private lateinit var chatsRecyclerView   : RecyclerView
    private lateinit var chatsLayoutManager  : RecyclerView.LayoutManager
    private lateinit var chatsAdapter        : ChatsAdapter
    private val chatsInfo                    = arrayListOf<ChatModal>()
    private lateinit var fstore              : FirebaseFirestore
    private lateinit var auth                : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view  = inflater.inflate(R.layout.fragment_chats, container, false)
        chatsRecyclerView                  = view.findViewById(R.id.chatsContentRecyclerView)
        chatsLayoutManager                 = LinearLayoutManager(context as Activity)
        auth                               = FirebaseAuth.getInstance()
        fstore                             = FirebaseFirestore.getInstance()
        fstore.collection("chats").whereArrayContains("uids",auth.currentUser!!.uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.d("", "")

                } else {
                    if (!snapshot?.isEmpty!!) {

                        chatsInfo.clear()

                        val ListContact = snapshot.documents
                        for (i in ListContact) {
                            fstore.collection("chats").document(i.id).collection("message")
                                .orderBy("messageId", Query.Direction.DESCENDING)
                                .addSnapshotListener { messagesnapshot, exception ->


                                    if (exception != null) {
                                        Log.d("error", "Some Error Occured")

                                    }
                                    else {
                                        if(!messagesnapshot!!.isEmpty)
                                        {
                                        val iid=i.id
                                        val id = messagesnapshot!!.documents[0]
                                        val message = id.get("message").toString()
                                        val receiver = id.get("meesageReceiver").toString()
                                        val obj =ChatModal(receiver, iid,message,"https://upload.wikimedia.org/wikipedia/commons/1/18/Manchester_United_v_Newcastle_United%2C_11_September_2021_%2810%29.jpg"
                                            )

                                           chatsInfo.add(obj)

                                    }
                                }
                                         chatsAdapter = ChatsAdapter(context as Activity, chatsInfo)
                                        chatsRecyclerView.adapter = chatsAdapter
                                        chatsRecyclerView.layoutManager = chatsLayoutManager
                                        chatsRecyclerView.addItemDecoration(
                                            DividerItemDecoration(
                                                chatsRecyclerView.context,
                                                (chatsLayoutManager as LinearLayoutManager).orientation))


//                                    }


                                }

                        }

                    }

                }

            }


        return view
    }

}