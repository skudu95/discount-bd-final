package com.kudu.discount_bd.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kudu.discount_bd.databinding.PostItemBinding
import com.kudu.discount_bd.model.Post

class PostAdapter(val context: Context, private val posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val profileImg = binding.profileImgPV
        val userName = binding.profileNamePV
        val postTime = binding.postTimePV
        val description = binding.descriptionPV
        val postImg = binding.postsImgPV
        val btnLike = binding.btnLikePV
        val btnUnlike = binding.btnUnlikePV
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostItemBinding.inflate(LayoutInflater.from(context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        holder.userName.text = post.publisher?.userName
        holder.description.text = post.description
        Glide.with(context).load(post.imageUrl).into(holder.postImg)
        holder.postTime.text = DateUtils.getRelativeTimeSpanString(post.creationTime)
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}