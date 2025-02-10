package com.olgunyilmaz.instagramclonekotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityUploadBinding
import java.net.URI
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    var selectedPicture : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

    }

    fun uploadClicked(view : View){
        if (selectedPicture != null){
            val reference = storage.reference
            val uuid = UUID.randomUUID()
            val imgName = "${uuid}.png"

            val imageReference = reference.child("images").child(imgName)

            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                // get download url -> firestore
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()

                    val postMap = hashMapOf<String,Any>()

                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("email",auth.currentUser!!.email!!.toString())
                    postMap.put("comment",binding.commentText.text.toString())
                    postMap.put("date",Timestamp.now())

                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }


            }.addOnFailureListener {
                Toast.makeText(this@UploadActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }



    }

    fun selectImage(view : View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermission(view,Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            requestPermission(view,Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun launchPermissionLauncher(permission : String){
        permissionLauncher.launch(permission)
    }

    private fun requestPermission(view : View,permission : String){
        if (ContextCompat.checkSelfPermission(this@UploadActivity, permission) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this@UploadActivity,permission)){
                Snackbar.make(view,"Permission needed for gallery!",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give permission",View.OnClickListener {
                        launchPermissionLauncher(permission)
                    }).show()
            }else{
                launchPermissionLauncher(permission)
            }

        }else{
            goToGallery()
        }


    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    val imageURI = intentFromResult.data
                    if (imageURI != null){
                        selectedPicture = imageURI
                        binding.imageView.setImageURI(selectedPicture)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if (result){
                goToGallery()
            }else{
                Toast.makeText(this@UploadActivity,"Permission needed!",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun goToGallery(){
        val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)

    }

}