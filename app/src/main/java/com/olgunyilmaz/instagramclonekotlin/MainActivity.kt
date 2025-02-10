package com.olgunyilmaz.instagramclonekotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    private lateinit var email : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null){
            goToFeedActivity()
        }
    }

    private fun goToFeedActivity(){
        val intent = Intent(this@MainActivity,FeedActivity :: class.java)
        startActivity(intent)
        finish()
    }

    fun signInClicked(view : View){
        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                goToFeedActivity()

            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this@MainActivity,"Enter email and password!",Toast.LENGTH_LONG).show()
        }

    }

    fun signUpClicked(view : View){
        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()

        if (email.isNotEmpty() &&  password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                // success
                val intent = Intent(this@MainActivity,FeedActivity :: class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this@MainActivity,"Enter email and password!",Toast.LENGTH_LONG).show()
        }
    }
}