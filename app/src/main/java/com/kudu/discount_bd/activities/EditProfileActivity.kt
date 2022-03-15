package com.kudu.discount_bd.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.discount_bd.databinding.ActivityEditProfileBinding
import com.kudu.discount_bd.model.Seller

/*import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await*/


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var seller: Seller
//    private val profileReference = firestoreDb.collection("sellers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        loadProfile()

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }

        //save button
        binding.btnSave.setOnClickListener {
       /*     val seller = getProfileData()
            saveSeller(seller)*/
            Toast.makeText(this, "Save Button clicked!", Toast.LENGTH_SHORT).show()

        }

        //logout button
        binding.btnLogout.setOnClickListener {

            Log.i(TAG, "Logout Clicked")
            auth.signOut()

            startActivity(Intent(this, StartActivity::class.java))
            finish()

        }
    }

  /*  private fun getProfileData(): Seller {
        val shopName = binding.shopName.text.toString()
        val userName = binding.userName.text.toString()
        val bio = binding.bio.text.toString()

        return Seller(shopName, userName, bio)
    }

    private fun getProfileDataMap(): Map<String, Any> {
        val shopName = binding.shopName.text.toString()
        val userName = binding.userName.text.toString()
        val bio = binding.bio.text.toString()
        val map = mutableMapOf<String, Any>()

        if (shopName.isNotEmpty()) {
            map["shopName"] = shopName
        }
        if (userName.isNotEmpty()) {
            map["userName"] = userName
        }
        if (bio.isNotEmpty()) {
            map["bio"] = bio
        }

        return map
    }


    private fun updateProfile(seller: Seller, updatedSellerMap: Map<String, Any>) =
        CoroutineScope(Dispatchers.IO).launch {
            val profileQuery = profileReference
                .whereEqualTo("shopName", seller.shopName)
                .whereEqualTo("userName", seller.userName)
                .whereEqualTo("bio", seller.bio)
                .get()
                .await()

            if (profileQuery.documents.isNotEmpty()) {
                for (document in profileQuery) {
                    try {
                        profileReference.document(document.id).set(
                            updatedSellerMap,
                            SetOptions.merge()
                        ).await()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this, "User not matched ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this, "User not matched ", Toast.LENGTH_SHORT).show()
                }
            }
        }

 *//*   private fun retrieveProfile() = CoroutineScope(Dispatchers.IO).launch {
        val fromAge = etFrom.text.toString().toInt()
        val toAge = etTo.text.toString().toInt()
        try {
            val querySnapshot = profileReference
                .whereGreaterThan("age", fromAge)
                .whereLessThan("age", toAge)
                .orderBy("age")
                .get()
                .await()
            val sb = StringBuilder()
            for (document in querySnapshot.documents) {
                val seller = document.toObject<Seller>()
                sb.append("$seller\n")
            }
            withContext(Dispatchers.Main) {
                .text = sb.toString()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }*//*


    private fun saveSeller(seller: Seller) = CoroutineScope(Dispatchers.IO).launch {
        try {
            profileReference.add(seller).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this, "Successfully saved data.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }*/

    private fun loadProfile() {

        val user = auth.currentUser
        firestoreDb = FirebaseFirestore.getInstance()
        val profileReference = firestoreDb.collection("sellers").document(user!!.uid)
        profileReference.get()
            .addOnSuccessListener { userSnapshot ->
                val user = userSnapshot.toObject(Seller::class.java)
                binding.userName.text = Editable.Factory.getInstance().newEditable(user?.userName)
                binding.shopName.text = Editable.Factory.getInstance().newEditable(user?.shopName)
                binding.bio.text = Editable.Factory.getInstance().newEditable(user?.bio)
                Toast.makeText(this, "Profile data fetched successfully!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "User: ${userSnapshot.data}")
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching profile details...", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Error fetching profile details", exception)
            }
    }

}