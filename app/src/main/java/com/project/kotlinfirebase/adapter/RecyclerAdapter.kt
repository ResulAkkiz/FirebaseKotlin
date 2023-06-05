package com.project.kotlinfirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.project.kotlinfirebase.databinding.ActivityFeedBinding
import com.project.kotlinfirebase.databinding.RecyclerRowBinding
import com.project.kotlinfirebase.model.Post
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val postList:ArrayList<Post>):RecyclerView.Adapter<RecyclerAdapter.PostHolder>()
{
    class PostHolder( val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        println(postList[position].userEmail)
        holder.binding.recyclerEmailText.text=postList[position].userEmail
        holder.binding.recyclerCommentText.text=postList[position].comment
        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.recyclerImageView)


    }

    override fun getItemCount(): Int {
       return  postList.size
    }
}