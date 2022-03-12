package com.kudu.discount_bd.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kudu.discount_bd.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }

        //save button
        binding.btnSave.setOnClickListener {

            val shopName = binding.shopName.text.toString()
            val userName = binding.username.text.toString()
            val bio = binding.bio.text.toString()

        }

        //logout button
        binding.btnLogout.setOnClickListener {

            Log.i(TAG, "Logout Clicked")
            auth.signOut()

            startActivity(Intent(this, StartActivity::class.java))
            finish()

        }
    }

}