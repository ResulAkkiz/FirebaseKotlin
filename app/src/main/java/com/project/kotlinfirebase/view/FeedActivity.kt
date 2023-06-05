package com.project.kotlinfirebase.view

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.kotlinfirebase.R
import com.project.kotlinfirebase.adapter.RecyclerAdapter
import com.project.kotlinfirebase.databinding.ActivityFeedBinding
import com.project.kotlinfirebase.model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postAdapter: RecyclerAdapter
    private val postList=ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firebaseAuth= FirebaseAuth.getInstance()
        firestore=Firebase.firestore
        getData()
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        postAdapter= RecyclerAdapter(postList)
        binding.recyclerView.adapter=postAdapter

    }

    private fun getData(){
       var x= firestore.collection("posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener{value,error->
           if (error !=null){
               Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
           }else{
               if (value !=null && !value.isEmpty){
                   val documents=value.documents
                   postList.clear()
                   documents.forEach { document ->
                     val comment=  document.get("comment") as String
                     val downloadUrl=  document.get("downloadUrl") as String
                     val userEmail=  document.get("userEmail") as String
                       val post=Post(comment,userEmail,downloadUrl)
                       postList.add(post)
                   }

                   postAdapter.notifyDataSetChanged()
               }
           }
       }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.add_post){
            startActivity(Intent(this, UploadActivity::class.java))
        }else if(item.itemId== R.id.sign_out){
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}