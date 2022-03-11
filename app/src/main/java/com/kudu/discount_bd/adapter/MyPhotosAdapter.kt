package com.kudu.discount_bd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kudu.discount_bd.databinding.MyPhotoCardviewBinding
import com.kudu.discount_bd.model.Post

class MyPhotosAdapter(val context: Context, private val posts: List<Post>) :
    RecyclerView.Adapter<MyPhotosAdapter.ViewHolder>() {
    class ViewHolder(binding: MyPhotoCardviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

            val myPhotos = binding.myPhotosIV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyPhotoCardviewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        Glide.with(context).load(post.imageUrl).into(holder.myPhotos)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}