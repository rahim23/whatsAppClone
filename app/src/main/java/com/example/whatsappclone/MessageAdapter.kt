package com.example.whatsappclone

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MessageAdapter(val context: Activity, val messageList:ArrayList<MessageModal>)
 :RecyclerView.Adapter<MessageAdapter.MessageViewHolder>()
{
    private val left  = 0
    private val right = 1

        class MessageViewHolder(view: View):RecyclerView.ViewHolder(view){
        val message          : TextView = view.findViewById(R.id.txtMessage)
        val time             : TextView = view.findViewById(R.id.txtTime)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MessageViewHolder {
       return if(viewType==right)
       {
           val   MessageView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_sender,parent,false)
           return MessageViewHolder(MessageView)
       }
        else
        {
            val   MessageView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_view_receiver,parent,false)
            return MessageViewHolder(MessageView)
        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val List           =  messageList[position]
        holder.message.text   =  List.message
        holder.time.text  =  List.timeStamp

    }

    override fun getItemCount(): Int {
        return  messageList.size

    }

    override fun getItemViewType(position: Int): Int {
        return if(messageList[position].sender==FirebaseAuth.getInstance().currentUser?.uid.toString())
        {
           left
        }
        else
        {
            right

        }



    }


}