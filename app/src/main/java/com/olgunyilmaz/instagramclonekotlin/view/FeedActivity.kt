package com.olgunyilmaz.instagramclonekotlin.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.olgunyilmaz.instagramclonekotlin.R
import com.olgunyilmaz.instagramclonekotlin.adapter.PostAdapter
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityFeedBinding
import com.olgunyilmaz.instagramclonekotlin.model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()
        fetchData()
        postAdapter = PostAdapter(postArrayList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postAdapter




    }

    private fun fetchData(){
        db.collection("Posts").addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        for (document in documents){
                            // casting
                            val comment = document.get("comment") as String
                            val email = document.get("email") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            val post = Post(email,comment,downloadUrl)

                            postArrayList.add(post)
                        }
                        postAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.insta_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post){
            val intent = Intent(this@FeedActivity, UploadActivity :: class.java)
            startActivity(intent)

        }else if (item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this@FeedActivity, MainActivity :: class.java)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }

}