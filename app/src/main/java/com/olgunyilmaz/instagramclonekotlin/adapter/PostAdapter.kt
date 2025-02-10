package com.olgunyilmaz.instagramclonekotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olgunyilmaz.instagramclonekotlin.databinding.RecyclerRowBinding
import com.olgunyilmaz.instagramclonekotlin.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(val postArrayList: ArrayList<Post>) : RecyclerView.Adapter <PostAdapter.PostHolder>() {

    class PostHolder (val binding : RecyclerRowBinding) : RecyclerView.ViewHolder (binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postArrayList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postArrayList.get(position).email
        holder.binding.recyclerCommentText.text = postArrayList.get(position).comment
        Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.binding.recyclerImageView)
    }
}