package com.kudu.discount_bd.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kudu.discount_bd.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            goHome()
        }

        //user btn
        binding.btnUserLogin.setOnClickListener {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        //seller btn
        binding.btnSellerLogin.setOnClickListener {
            val intent = Intent(this, SellerLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goHome() {
        Log.i(ContentValues.TAG, "goHome")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}