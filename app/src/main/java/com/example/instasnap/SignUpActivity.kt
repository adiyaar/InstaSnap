package com.example.instasnap

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signin_link_btn.setOnClickListener {
            startActivity(Intent(this,SignInActivity:: class.java))

        }

        signup_btn.setOnClickListener {
            CreateAccount()
        }
    }

    private fun CreateAccount() {

        val fullname = fullname_signup.text.toString()
        val username = username_signup.text.toString()
        val email = email_signup.text.toString()
        val password = password_signup.text.toString()

        when{
            TextUtils.isEmpty((fullname)) ->  Toast.makeText(this,"Full name required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty((username)) ->  Toast.makeText(this,"Username required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty((email)) ->  Toast.makeText(this,"Email Id required",Toast.LENGTH_LONG).show()
            TextUtils.isEmpty((password)) ->  Toast.makeText(this,"Password required",Toast.LENGTH_LONG).show()

            else -> {
                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("Joining In")
                progressDialog.setMessage("Please Wait Until we add you ")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mauth: FirebaseAuth = FirebaseAuth.getInstance()
                mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                    if(task.isSuccessful)
                    {
                        saveUserInfo(fullname,username,email,progressDialog)
                    }
                    else
                    {
                        val message = task.exception.toString()
                         Toast.makeText(this,"Error : $message",Toast.LENGTH_LONG).show()
                        mauth.signOut()
                        progressDialog.dismiss()
                    }
                }

            }
        }

    }

    private fun saveUserInfo(fullname: String, username: String, email: String,progressDialog: ProgressDialog)
    {
        val currentuserID = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val usermap = HashMap<String, Any>()
            usermap["uid"] = currentuserID
            usermap["fullname"] = fullname.toLowerCase()
            usermap["username"] = username.toLowerCase()
            usermap["email"] = email
            usermap["bio"] = "Hey I am there on InstaSnap Lets be Friends?"
            usermap["image"] ="https://firebasestorage.googleapis.com/v0/b/instasnap-926d2.appspot.com/o/Defaults%2Fprofile.png?alt=media&token=99d703c6-dd60-4606-be50-59f82be0ed2a"

            userRef.child(currentuserID).setValue(usermap)
                .addOnCompleteListener{task ->

                    if(task.isSuccessful){
                        progressDialog.dismiss()
                        Toast.makeText(this,"Account Created",Toast.LENGTH_LONG).show()

                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        val message = task.exception.toString()
                        Toast.makeText(this,"Error : $message",Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }



    }
}
