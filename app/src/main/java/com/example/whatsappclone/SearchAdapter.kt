package com.example.whatsappclone

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(val context: Context,private val searchList:ArrayList<User>)
    : RecyclerView.Adapter<SearchAdapter.searchViewHolder> (){

    class searchViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name          : TextView  = view.findViewById(R.id.txtName)
        val email         : TextView  = view.findViewById(R.id.txtEmail)
        val status        : TextView  = view.findViewById(R.id.txtStatus)
        val image         : ImageView = view.findViewById(R.id.imgprofile_image)
        val addFriend     : Button    = view.findViewById(R.id.btAddFriend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchViewHolder {
        val    searchView = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_contacts,parent,false)
        return searchViewHolder (searchView)
    }

    override fun onBindViewHolder(holder: searchViewHolder, position: Int) {
        val List                    =  searchList[position]
        holder.name.text            =  List.ProfileName
        holder.email.text           =  List.ProfileEmail
        holder.status.text          =  List.ProfileStatus
     //   holder.addFriend.visibility =  View.VISIBLE
        Picasso.get()
            .load(List.ProfilePicture)
            .error(R.drawable.profile) .into(holder.image)
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("friends").whereEqualTo(FieldPath.documentId(),List.ProfileUid)
            .addSnapshotListener{ snapshot,exception->
                if(exception!=null){

                    Log.d("onError","Some Error Occured")
                }

                else
                {
                  if(!snapshot?.isEmpty!!){
                      holder.addFriend.visibility = View.GONE

                  }
                    else {
                      holder.addFriend.visibility = View.VISIBLE


                  }


                }

            }



       holder.addFriend.setOnClickListener {
              val c          = Calendar.getInstance()
              val hour       = c.get(Calendar.HOUR)
              val minute     = c.get(Calendar.MINUTE)
              val timeStamp  = "$hour$minute"
              val obj        = mutableMapOf<String,String>().also {
                  it["time"] = timeStamp
              }
             val uid1        = FirebaseAuth.getInstance().currentUser?.uid.toString()
             val uid2        = List.ProfileUid
              FirebaseFirestore.getInstance()
             .collection("users")
                  .document(uid1)
                  .collection("friends").document(uid2).set(obj).addOnSuccessListener {
                      Log.d("onSuccess","Successfully Aded With $uid2 ")
                  }
             val obj1 = mutableMapOf<String,ArrayList<String>>().also {
                 it["uids"] = arrayListOf(uid1,uid2)

             }
           FirebaseFirestore.getInstance().collection("chats").document().set(obj1)
               .addOnSuccessListener {
                   Log.d("onSuccess","Successfully chat created With $uid2 ")

               }



       }





    }

    override fun getItemCount(): Int {
        return  searchList.size

    }



}
