package com.kudu.discount_bd.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.kudu.discount_bd.databinding.ActivityUserRegistrationBinding

class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserRegistrationBinding
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //button register
        binding.customerRegistrationBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {

        val userName = binding.userNameET.text.toString()
        val fullName = binding.fullNameET.text.toString()
        val email = binding.customerEmailEt.text.toString()
        val password = binding.customerPassEt.text.toString()

        if (userName.isBlank() || fullName.isBlank()  || email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show()
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserInfo(userName, fullName, email)
                    Log.i(ContentValues.TAG, "Account created with id: ${auth.currentUser?.uid} + username: $userName")
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SellerLoginActivity::class.java))
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Log.e(ContentValues.TAG, "Error: $message")
                    Toast.makeText(this, "Account could not be created, please try again...", Toast.LENGTH_LONG).show()
                    auth.signOut()

                }
            }
    }

    private fun saveUserInfo(userName: String, fullName: String, email: String) {
        val currentUserID = auth.currentUser!!.uid
        firestoreDb = FirebaseFirestore.getInstance()

        val userMap = hashMapOf<String, Any>(
            "uid" to currentUserID,
            "userName" to userName,
            "fullName" to fullName,
            "email" to email,
            "profileImage" to "https://firebasestorage.googleapis.com/v0/b/discount-bd.appspot.com/o/Default%20Images%2Fprofile_img.png?alt=media&token=fb06bdaa-9d09-453a-8e62-3f0bb11af5b6"

        )

        firestoreDb.collection("users")
            .document(currentUserID)
            .set(userMap)
//            .add(sellerMap)
            .addOnSuccessListener {
//                Log.i(TAG, "Added document with ID ${it.id}")
                Log.i(ContentValues.TAG, "Added document with ID $currentUserID")
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error adding document $exception")
            }
    }
}