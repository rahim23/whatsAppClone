package com.example.whatsappclone

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class SignUp : Fragment() {
    private lateinit var enterEmail      : TextInputEditText
    private lateinit var enterPassword   : TextInputEditText
    private lateinit var confirmPassword : TextInputEditText
    private lateinit var signUpButton    : Button
    private lateinit var fauth           : FirebaseAuth
    private lateinit var fstore          : FirebaseFirestore
    private lateinit var db              : DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup, container, false)
        enterEmail      = view.findViewById(R.id.etSignUpEmail)
        enterPassword   = view.findViewById(R.id.etsignuppassword)
        confirmPassword = view.findViewById(R.id.etsignupconfirmpassword)
        signUpButton    = view.findViewById(R.id.btsignup)
        fauth           = FirebaseAuth.getInstance()
        fstore          = FirebaseFirestore.getInstance()

        signUpButton.setOnClickListener {
            val email           = enterEmail.text.toString()
            val password        = enterPassword.text.toString()
            val confirmPasswor = confirmPassword.text.toString()

            if(TextUtils.isEmpty(email))
            {
                enterEmail.error = " Email is Required To Create Account "
            }
            else if(TextUtils.isEmpty(password))
            {
                enterPassword.error = " Password is Required to Create Account"
            }
            else if(password.length<6)
            {
                enterPassword.error ="Password must be greater than 6 characters in size"

            }
            else if(password!=confirmPasswor)
            {
                confirmPassword.error=" Both passwords not matched"
            }
            else
            {
                createAccount(email,password)
            }


        }

        return view
    }

private fun createAccount(em:String,pass:String ) {
    fauth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener{task->
    if(task.isSuccessful){
        val userInfo        = fauth.currentUser?.uid
        db                  = fstore.collection("users").document(userInfo.toString())
        val obj             = mutableMapOf<String,String>()
        obj["userEmail"]    = em
        obj["userPassword"] = pass
        obj["userStatus"]   = ""
        obj["userName"]     = ""
        obj["userPhoto"]    = "https://upload.wikimedia.org/wikipedia/commons/1/18/Manchester_United_v_Newcastle_United%2C_11_September_2021_%2810%29.jpg"
        db.set(obj).addOnSuccessListener{
            Log.d("onSucces","User Created Successfully")

        }

    }




    }

}



}