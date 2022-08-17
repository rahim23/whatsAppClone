package com.example.whatsappclone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class Login : Fragment() {
    private lateinit var enterEmail          : TextInputEditText
    private lateinit var enterPassword       : TextInputEditText
    private lateinit var loginButton         : Button
    private lateinit var googleButton        : ImageButton
    private lateinit var googleSignInOptions : GoogleSignInOptions
   // private lateinit var mGoogleSignInClient : GoogleSignInAccount
   // private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var resultLaunch        : ActivityResultLauncher<Intent>
    private var RC_SIGN_IN                   = 1011

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view        = inflater.inflate(R.layout.fragment_login, container, false)
      enterEmail      = view.findViewById(R.id.etLoginEmail)
      enterPassword   = view.findViewById(R.id.etLoginPassword)
      loginButton     = view.findViewById(R.id.btlogin)
      googleButton    = view.findViewById(R.id.btgoogleLogin)

      loginButton.setOnClickListener {
          val email           = enterEmail.text.toString()
          val password        = enterPassword.text.toString()

          if(TextUtils.isEmpty(email))
          {
              enterEmail.error = " Email is Required To SignIn Account "
          }
          else if(TextUtils.isEmpty(password))
          {
              enterPassword.error = " Password is Required to SignIn Account"
          }

          else
          {
              SignIn(email,password)

          }

      }

      googleButton.setOnClickListener {
          createRequest()

      }
/*
        resultLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
          if(result.resultCode==Activity.RESULT_OK){
              val launchData = result.data
              val task = GoogleSignIn.getSignedInAccountFromIntent(launchData)


              try{
                       val account = task.getResult(ApiException::class.java)
                       Log.d("Gmail ID","firebaseAuthWith Google : $account")
                       firebaseAuthWithGoogle(account?.idToken)
                   }
                   catch(e:ApiException)
                   {
                       Log.d("Error","Google SignIN Failed",e)
                   }

          }



        }
*/

        return view
    }

    private fun createRequest(){
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("Gooogle ID")
            .requestEmail()
            .build()
    }


   private fun  firebaseAuthWithGoogle(idToken: String?){

   }

   private fun SignIn(em:String,pass:String){
       FirebaseAuth.getInstance().signInWithEmailAndPassword(em,pass).addOnCompleteListener{task->
           if(task.isSuccessful)
           {
               Toast.makeText(context,"loginSuccessfuly ",Toast.LENGTH_LONG).show()


           }
           else
           {

               Toast.makeText(context,"Email or Password is incorecct  ",Toast.LENGTH_LONG).show()


           }


       }



   }



}