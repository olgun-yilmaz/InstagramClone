package com.olgunyilmaz.instagramclonekotlin

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    fun uploadClicked(view : View){

    }

    fun selectImage(view : View){

    }
}