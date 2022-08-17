package com.example.whatsappclone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ChatsAdapter (val context: Context, val chatslist:ArrayList<ChatModal>)
    : RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder> (){

    class ChatsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name          : TextView = view.findViewById(R.id.txtReceiverName)
        val message         : TextView = view.findViewById(R.id.txtMessage)
        val content        : CardView = view.findViewById(R.id.chatContent)
        val image         : ImageView = view.findViewById(R.id.imgchat_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsAdapter.ChatsViewHolder {
        val    chatsView = LayoutInflater.from(parent.context).inflate(R.layout.chat_view,parent,false)
        return ChatsViewHolder(chatsView)
    }



    override fun onBindViewHolder(holder: ChatsAdapter.ChatsViewHolder, position: Int) {
        val List             =  chatslist[position]

        holder.name.text     =  List.receiver
        holder.message.text  =  List.message
        Picasso.get()
            .load(List.recieverImge)
            .error(R.drawable.profile)
            .into(holder.image)
        holder.content.setOnClickListener {
            val intent = Intent(context,MenuActivity::class.java).also {
                it.putExtra("OptionName","ChatMessaging")
                it.putExtra("chatRoom",List.docID)
                it.putExtra("ReceiverName",List.receiver)


            }
            context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return  chatslist.size

    }



}
