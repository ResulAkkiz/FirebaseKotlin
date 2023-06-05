package com.project.kotlinfirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.project.kotlinfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var email=""
    private var password=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firebaseAuth=FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, FeedActivity::class.java))
            finish()
        }

    }

    fun signinClicked(view: View){
        email=binding.emailText.text.toString()
        password=binding.passwordText.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent=Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this,"Error: ${it.localizedMessage}",Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this,"Please enter valid email and password.",Toast.LENGTH_LONG).show()
        }

    }


    fun signupClicked(view: View){
         email=binding.emailText.text.toString()
         password=binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent=Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this,"Error: ${it.localizedMessage}",Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this,"Please enter valid email and password.",Toast.LENGTH_LONG).show()
        }




    }
}