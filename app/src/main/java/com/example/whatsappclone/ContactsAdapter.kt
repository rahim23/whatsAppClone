package com.example.whatsappclone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ContactsAdapter(val context:Context,val contactList:ArrayList<User>)
    :  RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> () {

/*
    private lateinit var mlistener: onItemClickListener

interface  onItemClickListener {
    fun onItemClick(position: Int)

}

fun setOnItemClickListener(listener:onItemClickListener ){
    mlistener = listener
}
*/

// class ContactsViewHolder(view: View,listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
  class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.txtName)
    val email: TextView = view.findViewById(R.id.txtEmail)
    val status: TextView = view.findViewById(R.id.txtStatus)
    val image: ImageView = view.findViewById(R.id.imgprofile_image)
    val userContent: CardView = view.findViewById(R.id.userContent)
/*
       init{
           view.setOnClickListener{
               listener.onItemClick(adapterPosition)
           }
*/

}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val contactView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_contacts, parent, false)

   //     return ContactsViewHolder(contactView,mlistener)
        return ContactsViewHolder(contactView)

    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int)
    {
        val List = contactList[position]
        holder.name.text = List.ProfileName
        holder.email.text = List.ProfileEmail
        holder.status.text = List.ProfileStatus
        Picasso.get().load(List.ProfilePicture).error(R.drawable.profile).into(holder.image)

        holder.userContent.setOnClickListener{v:View->

            val intent  =  Intent(this.context,MenuActivity::class.java)

                intent.putExtra("OptionName", "contactMessaging")
                intent.putExtra("chatRoom", List.chatRoomID)
                intent.putExtra("friendUID",List.ProfileUid)
                intent.putExtra("friendName", List.ProfileName)
            this.context.startActivity(intent)


       }


    }

    override fun getItemCount(): Int {
         return contactList.size

    }

}

