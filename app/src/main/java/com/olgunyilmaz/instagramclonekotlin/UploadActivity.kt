package com.olgunyilmaz.instagramclonekotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.olgunyilmaz.instagramclonekotlin.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    var selectedBitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()

    }

    fun uploadClicked(view : View){

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
                        try{
                            if (Build.VERSION.SDK_INT >= 28){
                                val source = ImageDecoder.createSource(this@UploadActivity.contentResolver, imageURI)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                            }else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(this@UploadActivity.contentResolver,imageURI)
                            }
                            binding.imageView.setImageBitmap(selectedBitmap)

                        }catch (e : Exception){
                            e.printStackTrace()
                        }
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