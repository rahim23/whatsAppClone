package com.example.whatsappclone

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.ResourceManagerInternal.get
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.okhttp.internal.Platform.get
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.reflect.Array.get
import javax.sql.DataSource


class Profile :Fragment() {

    private lateinit var ProfileNameShow   : TextView
    private lateinit var ProfileEmailShow  : TextView
    private lateinit var ProfileStatusShow : TextView
    private lateinit var ProfilePicture    : AppCompatImageView
    private lateinit var ProfilePictureAdd : ImageView
    private lateinit var ProfileNameEdit   : TextInputLayout
    private lateinit var ProfileEmailEdit  : TextInputLayout
    private lateinit var ProfileStatusEdit : TextInputLayout
    private lateinit var editName          : TextInputEditText
    private lateinit var editEmail         : TextInputEditText
    private lateinit var editStatus        : TextInputEditText
    private lateinit var ProfileUpdate     : Button
    private lateinit var ProfileSave       : Button
    private lateinit var auth              : FirebaseAuth
    private lateinit var fstore            : FirebaseFirestore
    private lateinit var db                : DocumentReference
    private lateinit var userid            : String
    private lateinit var image             : ByteArray
    private lateinit var storageReference  : StorageReference
    val register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
        uploadImage(it)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view                 = inflater.inflate(R.layout.fragment_profile, container, false)

        auth                     = FirebaseAuth.getInstance()
        fstore                   = FirebaseFirestore.getInstance()
        userid                   = auth.currentUser!!.uid
        storageReference         = FirebaseStorage.getInstance().reference.child("$userid/profilePhoto")
        editName                 = view.findViewById(R.id.etProfileName)
        editEmail                = view.findViewById(R.id.etProfileEmail)
        editStatus               = view.findViewById(R.id.etProfileStatus)
        ProfileNameEdit          = view.findViewById(R.id.profile_name)
        ProfileNameShow          = view.findViewById(R.id.txtProfileName)
        ProfileEmailEdit         = view.findViewById(R.id.profile_email)
        ProfileEmailShow         = view.findViewById(R.id.txtProfileEmail)
        ProfileStatusEdit        = view.findViewById(R.id.profile_Status)
        ProfileStatusShow        = view.findViewById(R.id.txtProfileStatus)
        ProfilePicture           = view.findViewById(R.id.imgprofile_image)
        ProfilePictureAdd        = view.findViewById(R.id.imgAddProfileImage)
        ProfileUpdate            = view.findViewById(R.id.btupdateprofile)
        ProfileSave              = view.findViewById(R.id.btsaveProfile)

        ProfileUpdate.visibility = View.VISIBLE
        db                       = fstore.collection("users").document(userid)
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("Error", "Unable to fetch data")
            }

            else
            {

                ProfileNameShow.text = value?.getString("userName")
                ProfileEmailShow.text = value?.getString("userEmail")
                ProfileStatusShow.text = value?.getString("userStatus")
                    // Glide.with(this)
                //    .load("gs://whatsappclone-b9b03.appspot.com/qY4HzQj07ahbMfA9lgs3kueWkgs1/profilePhoto")
                  //  .load((value?.getString("userProfilePhoto"))?.toUri()?.buildUpon()?.scheme("HTTPS")?.build())
                //    .centerCrop()
                   //.load("https://upload.wikimedia.org/wikipedia/commons/1/18/Manchester_United_v_Newcastle_United%2C_11_September_2021_%2810%29.jpg")
                   // .error(R.drawable.profile)
                   // .into(ProfilePicture)

            }


        }

        ProfileUpdate.setOnClickListener {
            ProfileNameShow.  visibility   = View.GONE
            ProfileEmailShow. visibility   = View.GONE
            ProfileStatusShow.visibility   = View.GONE

            ProfileNameEdit.  visibility   = View.VISIBLE
            ProfileEmailEdit. visibility   = View.VISIBLE
            ProfileStatusEdit.visibility   = View.VISIBLE
            ProfileSave      .visibility   = View.VISIBLE
            ProfileUpdate    .visibility   = View.GONE
            editName.text                  = Editable.Factory.getInstance().newEditable(ProfileNameShow.text.toString())
            editEmail.text                 = Editable.Factory.getInstance().newEditable(ProfileEmailShow.text.toString())
            editStatus.text                = Editable.Factory.getInstance().newEditable(ProfileStatusShow.text.toString())

        }

         ProfileSave.setOnClickListener {
            ProfileNameShow.  visibility   = View.VISIBLE
            ProfileEmailShow. visibility   = View.VISIBLE
            ProfileStatusShow.visibility   = View.VISIBLE

            ProfileNameEdit.  visibility   = View.GONE
            ProfileEmailEdit. visibility   = View.GONE
            ProfileStatusEdit.visibility   = View.GONE
            ProfileSave      .visibility   = View.GONE
            ProfileUpdate    .visibility   = View.VISIBLE
             //ProfileNameShow.text          = Editable.Factory.getInstance().newEditable(editName.text.toString())
            // ProfileEmailShow.text         = Editable.Factory.getInstance().newEditable(editEmail.text.toString())
            // ProfileStatusShow.text        = Editable.Factory.getInstance().newEditable(editStatus.text.toString())
             val obj                        = mutableMapOf<String,String>()
             obj["userName"]                = editName.text.toString()
             obj["userEmail"]               = editEmail.text.toString()
             obj["userStatus"]              = editStatus.text.toString()
             db.update(obj as Map<String,String>).addOnSuccessListener{
                 Log.d("onSucces","User Created Successfully")

             }
             ProfilePictureAdd.setOnClickListener {
                 capturePhoto()

             }


         }


     return view
    }


    private fun capturePhoto() {
//    val  register = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
  //      uploadImage(it)

   // }
    register.launch(null)

}
private fun uploadImage(it:Bitmap?){
    val baos     = ByteArrayOutputStream()
    it?.compress(Bitmap.CompressFormat.JPEG,50,baos)
    image        = baos.toByteArray()
    storageReference.putBytes(image).addOnCompleteListener{
        storageReference.downloadUrl.addOnCompleteListener{
            val obj = mutableMapOf<String,String>()
            obj["userProfilePhoto"] = it.toString()
            db.update(obj as Map<String,String>).addOnSuccessListener {
                Log.d("onSucces" ,"ProfulePictureUploaded")

            }

        }


    }

}



}