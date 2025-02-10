package com.olgunyilmaz.instagramclonekotlin

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

}