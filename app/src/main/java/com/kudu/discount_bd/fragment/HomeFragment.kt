package com.kudu.discount_bd.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kudu.discount_bd.R
import com.kudu.discount_bd.adapter.PostAdapter
import com.kudu.discount_bd.databinding.FragmentHomeBinding
import com.kudu.discount_bd.model.Post


class HomeFragment : Fragment() {

    companion object {
        lateinit var binding: FragmentHomeBinding
        lateinit var posts: MutableList<Post>

    }

    private lateinit var postAdapter: PostAdapter
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)

        //datasource
        posts = mutableListOf()

        //adapter
        postAdapter = PostAdapter(requireContext(), posts)
        binding.postRV.setHasFixedSize(true)
        binding.postRV.setItemViewCacheSize(20)
        binding.postRV.adapter = postAdapter
        binding.postRV.layoutManager = LinearLayoutManager(context)

        firestoreDb = FirebaseFirestore.getInstance()
        val postsReference = firestoreDb.collection("posts")
            .limit(20)
            .orderBy("creationTime", Query.Direction.DESCENDING)

        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            postAdapter.notifyDataSetChanged()
            for (post in postList) {
                Log.d(TAG, "Post: $post")
            }
        }

        return view
    }


}