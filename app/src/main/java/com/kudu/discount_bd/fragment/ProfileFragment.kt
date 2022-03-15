package com.kudu.discount_bd.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kudu.discount_bd.R
import com.kudu.discount_bd.activities.EditProfileActivity
import com.kudu.discount_bd.adapter.MyPhotosAdapter
import com.kudu.discount_bd.databinding.FragmentProfileBinding
import com.kudu.discount_bd.model.Post
import com.kudu.discount_bd.model.Seller
import com.kudu.discount_bd.model.User

class ProfileFragment : Fragment() {

    companion object {
        lateinit var binding: FragmentProfileBinding
        lateinit var myPhotos: MutableList<Post>
        private lateinit var auth: FirebaseAuth
    }

    private lateinit var myPhotosAdapter: MyPhotosAdapter
    private lateinit var firestoreDb: FirebaseFirestore

    //test profile view
    private var signedInUser: User? = null
    private var signedInSeller: Seller? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.bind(view)

        auth = FirebaseAuth.getInstance()

        loadProfile()
        loadMyPhotos()

      /*  if (signedInUser!!.uid == auth.currentUser!!.uid){
            loadCustomerProfile()
        }
        if (signedInSeller!!.uid == auth.currentUser!!.uid){
            loadProfile()
            loadMyPhotos()
        }
*/
//        loadCustomerProfile()
        /*    val customer = auth.currentUser
            firestoreDb = FirebaseFirestore.getInstance()
            val customerReference =
                firestoreDb.collection("users").document("id")

            if (signedInUser != null) {
                if (customer!!.uid != customerReference.toString()) {
                    loadProfile()
                    loadMyPhotos()
                } else {
                    loadCustomerProfile()
                }
            }*/

      /*  val loggedInUser = auth.currentUser
        firestoreDb = FirebaseFirestore.getInstance()
        val customersReference = firestoreDb.collection("users").document("uid")
        val sellersReference = firestoreDb.collection("sellers").document("uid")

        if (loggedInUser?.uid == customersReference.toString()) {
            loadCustomerProfile()
        } else if (loggedInUser?.uid == sellersReference.toString()){
            loadProfile()
            loadMyPhotos()
        }*/

        //button edit profile
        binding.btnEditProfile.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        myPhotos = mutableListOf()

        //adapter
//        postAdapter = PostAdapter(requireContext(), posts)
        myPhotosAdapter = MyPhotosAdapter(requireContext(), myPhotos)
        binding.myPhotosRV.setHasFixedSize(true)
        binding.myPhotosRV.setItemViewCacheSize(20)
        binding.myPhotosRV.adapter = myPhotosAdapter
//        binding.myPhotosRV.layoutManager = LinearLayoutManager(context)
        binding.myPhotosRV.layoutManager = GridLayoutManager(context, 2)

        /*firestoreDb = FirebaseFirestore.getInstance()
        val myPhotosReference = firestoreDb.collection("posts")
            .limit(20)
            .orderBy("creationTime", Query.Direction.DESCENDING)
            .whereEqualTo("publisher.uid", "${auth.currentUser?.uid}")

        */
        /*val signedInUser = auth.currentUser
        if (signedInUser != null){
            postsReference = postsReference.whereEqualTo("publisher.userName", signedInUser)
        }*//*
        myPhotosReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            myPhotos.clear()
            myPhotos.addAll(postList)
            myPhotosAdapter.notifyDataSetChanged()
            for (post in postList) {
                Log.d(TAG, "Post: $post")
            }
        }*/

        return view
    }

    private fun loadMyPhotos() {
        firestoreDb = FirebaseFirestore.getInstance()
        val myPhotosReference = firestoreDb.collection("posts")
            .limit(20)
            .orderBy("creationTime", Query.Direction.DESCENDING)
            .whereEqualTo("publisher.uid", "${auth.currentUser?.uid}")

        /*val signedInUser = auth.currentUser
        if (signedInUser != null){
            postsReference = postsReference.whereEqualTo("publisher.userName", signedInUser)
        }*/
        myPhotosReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            myPhotos.clear()
            myPhotos.addAll(postList)
            myPhotosAdapter.notifyDataSetChanged()
            for (post in postList) {
                Log.d(TAG, "Post: $post")
            }
        }
    }

    private fun loadCustomerProfile() {
        val customer = auth.currentUser
        firestoreDb = FirebaseFirestore.getInstance()
        val customerReference =
            firestoreDb.collection("users").document(customer!!.uid)
        customerReference.get()
            .addOnSuccessListener { userSnapshot ->
                val customer = userSnapshot.toObject(User::class.java)
                binding.userName.text = customer?.userName.toString()
                binding.shopNameTV.text = customer?.fullName.toString()
                binding.bio.visibility = View.INVISIBLE
                Toast.makeText(
                    context,
                    "Customer Profile data fetched successfully",
                    Toast.LENGTH_SHORT
                ).show()
                Log.i(TAG, "Customer: ${userSnapshot.data}")
            }
            .addOnFailureListener { exception ->

//                loadProfile()

                Toast.makeText(
                    context,
                    "Error fetching customer profile details...",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "Error fetching customer profile details", exception)
            }

    }

    private fun loadProfile() {

        val user = auth.currentUser
        firestoreDb = FirebaseFirestore.getInstance()
        val profileReference = firestoreDb.collection("sellers").document(user!!.uid)
        profileReference.get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(Seller::class.java)
                binding.userName.text = user?.userName.toString()
                binding.shopNameTV.text = user?.shopName.toString()
                binding.bio.text = user?.bio.toString()
                Toast.makeText(context, "Profile data fetched successfully!", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "User: ${userSnapshot.data}")
            }
            .addOnFailureListener { exception ->

//                loadCustomerProfile()

                Toast.makeText(context, "Error fetching profile details...", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "Error fetching profile details", exception)
            }
    }


}