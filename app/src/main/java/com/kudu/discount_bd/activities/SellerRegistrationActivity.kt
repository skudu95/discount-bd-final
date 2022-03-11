package com.kudu.discount_bd.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.kudu.discount_bd.databinding.ActivitySellerRegistrationBinding

class SellerRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerRegistrationBinding
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding = ActivitySellerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //button register
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val userName = binding.userNameET.text.toString()
        val shopName = binding.shopNameET.text.toString()
        val bio = binding.bioET.text.toString()
        val email = binding.emailET.text.toString()
        val password = binding.passwordET.text.toString()

        if (userName.isBlank() || shopName.isBlank() || bio.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show()
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveSellerInfo(userName, shopName, bio, email)
                    Log.i(TAG, "Account created with id: ${auth.currentUser?.uid} + username: $userName")
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SellerLoginActivity::class.java))
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Log.e(TAG, "Error: $message")
                    Toast.makeText(this, "Account could not be created, please try again...", Toast.LENGTH_LONG).show()
                    auth.signOut()

                }
            }
    }

    private fun saveSellerInfo(userName: String, shopName: String, bio: String, email: String) {
        val currentUserID = auth.currentUser!!.uid
        firestoreDb = FirebaseFirestore.getInstance()

        val sellerMap = hashMapOf<String, Any>(
            "uid" to currentUserID,
            "userName" to userName,
            "shopName" to shopName,
            "bio" to bio,
            "email" to email,
            "profileImage" to "https://firebasestorage.googleapis.com/v0/b/discount-bd.appspot.com/o/Default%20Images%2Fprofile_img.png?alt=media&token=fb06bdaa-9d09-453a-8e62-3f0bb11af5b6"

        )

        firestoreDb.collection("sellers")
            .document(currentUserID)
            .set(sellerMap)
//            .add(sellerMap)
            .addOnSuccessListener {
//                Log.i(TAG, "Added document with ID ${it.id}")
                Log.i(TAG, "Added document with ID $currentUserID")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error adding document $exception")
            }
    }
}