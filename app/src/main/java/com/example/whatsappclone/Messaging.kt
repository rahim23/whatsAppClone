package com.example.whatsappclone

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.*

class Messaging : Fragment() {
    private lateinit var messageRecyclerView : RecyclerView
    private lateinit var sendMessageEditText : EditText
    private lateinit var sendMessageButton   : FloatingActionButton
    private lateinit var fstore              : FirebaseFirestore
    private lateinit var auth                : FirebaseAuth
    private lateinit var messageLayoutManager: RecyclerView.LayoutManager
    private lateinit var messageAdapter      : MessageAdapter
    private lateinit var db                  : DocumentReference
    private lateinit var db1                  : DocumentReference
    private lateinit var userId              : String
    private lateinit var friendId            : String
    private var register                     : ListenerRegistration?=null
    private var register1                    : ListenerRegistration?=null
    private lateinit var chatroomId          : String
    private lateinit var chatroomUId         : String
    private lateinit var chatId              : String
    private val messageInfo                  = arrayListOf<MessageModal>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view             = inflater.inflate(R.layout.fragment_messaging, container, false)
        messageRecyclerView  = view.findViewById(R.id.messageRecycleView)
        sendMessageButton    = view.findViewById(R.id.btnSendMessage)
        sendMessageEditText  = view.findViewById(R.id.etSendMessage)

        val values = arguments
        if(values!=null)
        {
            friendId   = values.getString("friendName").toString()
            chatroomId = values.getString("documentID").toString()
            initialization(chatroomId)
        }
        val contactBundle = arguments

        if(contactBundle!=null)
        {
            friendId   = values!!.getString("friendName").toString()
            chatroomId = values.getString("chatRoomID").toString()
            fetchChatRoomUID()
        }

//         sendMessageButton.setOnClickListener {
   //          fetchMessageID()

      //   }
         return view
    }
private fun fetchChatRoomUID(){
    fstore.collection("chats").whereEqualTo("chatroomid",chatroomId).get().addOnSuccessListener {query->
         if(!query.isEmpty)
         {
             chatroomUId = query.documents[0].id
             initialization(chatroomUId)
             sendMessageButton.setOnClickListener {
                 fetchMessageID(chatroomUId )

             }

         }


    }

}



private fun fetchingMessage(idMessage: String)
{
    register1 = fstore.collection("chats").document(idMessage)
        .collection("message")
        .orderBy("messageId",Query.Direction.ASCENDING)
        .addSnapshotListener { snapshot, exception ->
            if (exception != null)
            {
                Log.d("", "")
            }
            else {
                messageInfo.clear()
                if (!snapshot?.isEmpty!!) {
                    var list = snapshot.documents
                    for (document in list) {
                        val obj = MessageModal(
                            document.getString("messageSender").toString(),
                            document.getString("message").toString(),
                            document.getString("messageTime").toString()
                        )
                        messageInfo.add(obj)


                    }
                    messageRecyclerView.scrollToPosition(messageInfo.size - 1)
                    messageRecyclerView.adapter!!.notifyDataSetChanged()

                }

            }

        }

}

private fun fetchMessageID(id:String) {
    db=fstore.collection("chats").document(id).collection("count").document("chatid")
    sendMessageButton.setOnClickListener {
        register = db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("onError", "Error in fetching Data")
            }
            else
            {
                chatId = value?.getString("chatid").toString()
                sendMessage()

            }
        }
    }



}


private fun initialization(id:String)
{
    fstore               = FirebaseFirestore.getInstance()
    auth                 = FirebaseAuth.getInstance()
    userId               = auth.currentUser?.uid.toString()
    messageLayoutManager = LinearLayoutManager(context)

    db1   = fstore.collection("chats").document(id).collection("message").document()
    recyclerViewBuild(id)


}



private fun recyclerViewBuild(id:String) {
    messageAdapter                      = MessageAdapter(context  as Activity,messageInfo)
    messageRecyclerView.adapter         = messageAdapter
    messageRecyclerView.layoutManager   = messageLayoutManager
    fetchingMessage(id)

}



private fun sendMessage() {
    val message = sendMessageEditText.text.toString()
    if(TextUtils.isEmpty(message))
    {
        sendMessageEditText.error = "Enter some Message to Send "

    }

    else
    {
       val c                     = Calendar.getInstance()
       val hour                  = c.get(Calendar.HOUR)
       val minute                = c.get(Calendar.MINUTE)
       val timeStamp             = "$hour:$minute"
           val messageObject     = mutableMapOf<String,Any>().also {
           it["message"]         = message
           it["messageId"]       = chatId
           it["messageSender"]   = userId
           it["meesageReceiver"] = friendId
           it["messageTime"]     = timeStamp

       }
      db1.set(messageObject)

    }
    val countid  = mutableMapOf<String,String>()
    countid["chatid"] = (chatId.toInt()+1).toString()
    db.set(countid)
}




override fun onDestroy() {
 register1!!.remove()
 super.onDestroy()

}







}