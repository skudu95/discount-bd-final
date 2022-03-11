package com.kudu.discount_bd.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.kudu.discount_bd.databinding.ActivitySellerLoginBinding


class SellerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellerLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySellerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            goHome()
        }

        //login button
        binding.sellerLoginBtn.setOnClickListener{
            binding.sellerLoginBtn.isEnabled = false // disables the login button after 1 click
            val email = binding.sellerEmailEtSignIn.text.toString()
            val password = binding.sellerPassEtSignIn.text.toString()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firebase authentication
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                binding.sellerLoginBtn.isEnabled = true //enabling the button after authentication
                if (task.isSuccessful){
                    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                    goHome()
                }else{
                    Log.e(TAG, "signInWithEmail failed", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //no seller account button
        binding.noSellerAccountTv.setOnClickListener {
            startActivity(Intent(this, SellerRegistrationActivity::class.java))
        }

    }

    private fun goHome() {
        Log.i(TAG, "goHome")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}