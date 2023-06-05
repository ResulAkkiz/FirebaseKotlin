package com.project.kotlinfirebase.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.project.kotlinfirebase.databinding.ActivityUploadBinding
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? =null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

        auth =Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage
    }

    fun upload(view: View){
        val imageId=UUID.randomUUID().toString()
        if (auth.currentUser !=null){
           val imageRef= storage.reference.child("postImages").child(auth.uid.toString()).child(imageId)
            if (selectedPicture !=null){
                imageRef.putFile(selectedPicture!!).addOnSuccessListener(){
                    imageRef.downloadUrl.addOnSuccessListener {uri->
                        val downloadUrl=uri.toString()
                        val postMap=HashMap<String,Any>()
                        postMap["downloadUrl"] = downloadUrl
                        postMap["date"] = Timestamp.now()
                        postMap["userEmail"] = auth.currentUser!!.email.toString()
                        postMap["comment"] = binding.commentText.text.toString()

                        firestore.collection("posts").add(postMap).addOnSuccessListener {
                            Toast.makeText(this,"Post's added successfully",Toast.LENGTH_LONG).show()
                            finish()

                        }.addOnFailureListener {exception->
                            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }
                }.addOnFailureListener(){exception->
                    Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    fun selectImage(view:View){
        val permission=android.Manifest.permission.READ_EXTERNAL_STORAGE
        val granted=PackageManager.PERMISSION_GRANTED
        if (ContextCompat.checkSelfPermission(this,permission) !=granted){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
                Snackbar.make(view,"Permission need for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"
                ) {
                    permissionResultLauncher.launch(permission)
                }.show()
                println("shouldShowRequestPermissionRationale's showing on screen")
            }
            else{
               permissionResultLauncher.launch(permission)
                println("else is triggered")

            }
        }else{
            println("Eğer izin verilmişse burası çalışır")
            val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }


    }

    private fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            println("ActivityResultCallback triggered")
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }

        permissionResultLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { result ->
            if (result) {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                Toast.makeText(this, "Permission Needed!", Toast.LENGTH_LONG).show()
            }
        }

    }
}