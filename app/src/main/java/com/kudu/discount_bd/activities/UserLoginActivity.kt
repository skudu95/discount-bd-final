package com.kudu.discount_bd.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kudu.discount_bd.databinding.ActivityUserLoginBinding

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            goHome()
        }

        //login button
        binding.customerLoginBtn.setOnClickListener{
            binding.customerLoginBtn.isEnabled = false // disables the login button after 1 click
            val email = binding.customerEmailEt.text.toString()
            val password = binding.customerPassEt.text.toString()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firebase authentication
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                binding.customerLoginBtn.isEnabled = true //enabling the button after authentication
                if (task.isSuccessful){
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    goHome()
                }else{
                    Log.e(ContentValues.TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //sign up btn
        binding.noCustomerAccountTv.setOnClickListener {
            startActivity(Intent(this, UserRegistrationActivity::class.java))
        }
    }

    private fun goHome() {
        Log.i(ContentValues.TAG, "goHome")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}