package com.kudu.discount_bd.fragment

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kudu.discount_bd.R
import com.kudu.discount_bd.activities.MainActivity
import com.kudu.discount_bd.databinding.FragmentAddPostBinding
import com.kudu.discount_bd.model.Post
import com.kudu.discount_bd.model.Seller

class AddPostFragment : Fragment() {

    companion object {
        lateinit var binding: FragmentAddPostBinding
        private var photoUri: Uri? = null
        private var signedInUser: Seller? = null
    }

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        binding = FragmentAddPostBinding.bind(view)

        storageReference = FirebaseStorage.getInstance().reference

        //signed in user
        firestoreDb = FirebaseFirestore.getInstance()
        firestoreDb.collection("sellers")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { sellerSnapshot ->
                signedInUser = sellerSnapshot.toObject(Seller::class.java)
                Log.i(TAG, "signed in user as: $signedInUser")
            }
            .addOnFailureListener{exception ->
                Log.e(TAG, "Failed to fetch signed-in user", exception)
            }

        //button close
        binding.btnClose.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }

        //image post
        binding.postImage.setOnClickListener {
            Log.i(TAG, "Image Picker opened")
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
//            if (imagePickerIntent.resolveActivity(context?.packageManager!!) != null){
            if (imagePickerIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivityForResult(imagePickerIntent, 1234)
            }
        }

        binding.btnPost.setOnClickListener {
            handlePostButtonClick()
        }

        return view
    }

    private fun handlePostButtonClick() {
        if (photoUri == null) {
            Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.description.text.isBlank()) {
            Toast.makeText(context, "Description not added", Toast.LENGTH_SHORT).show()
            return
        }
        if (signedInUser == null){
            Toast.makeText(context, "User not signed in", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnPost.isEnabled = false
        val photoUploadUri = photoUri as Uri
        val photoRef = storageReference.child("post-images/${System.currentTimeMillis()}-photo.jpg")
        //uploading image to Firestore storage
        photoRef.putFile(photoUploadUri)
            .continueWithTask{photoUploadTask ->
                Log.i(TAG, "uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                //retrieving image url of uploaded image
                photoRef.downloadUrl
            }
            .continueWithTask { downloadUrlTask ->
                //creating post object with image url and adding to collection
                val post = Post(
                    binding.description.text.toString(),
                    System.currentTimeMillis(),
                    downloadUrlTask.result.toString(),
                    signedInUser)
                firestoreDb.collection("posts").add(post)
            }.addOnCompleteListener { postCreationTask ->
                binding.btnPost.isEnabled = true
                if (!postCreationTask.isSuccessful){
                    Log.e(TAG, "Exception during Firebase operations", postCreationTask.exception)
                    Toast.makeText(context, "Failed to save post", Toast.LENGTH_SHORT).show()
                }
                binding.description.text.clear()
                binding.postImage.setImageResource(0)
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                /*val mainIntent = Intent(context, MainActivity::class.java)
                startActivity(mainIntent)*/

                //TODO: check which works
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentContainer, ProfileFragment())
                transaction?.disallowAddToBackStack()
                transaction?.commit()
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                Log.i(TAG, "photoUri: $photoUri")
                binding.postImage.setImageURI(photoUri)
            } else {
                Toast.makeText(
                    context,
                    "Image picker cancelled!! Please try again..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}