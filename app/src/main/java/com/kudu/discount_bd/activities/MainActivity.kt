package com.kudu.discount_bd.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.discount_bd.R
import com.kudu.discount_bd.databinding.ActivityMainBinding
import com.kudu.discount_bd.fragment.AddPostFragment
import com.kudu.discount_bd.fragment.HomeFragment
import com.kudu.discount_bd.fragment.ProfileFragment
import com.kudu.discount_bd.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var selectorFragment: Fragment? = null

    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //nav view
        val navView: BottomNavigationView = binding.bottomNav

        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        val intent = intent.extras
        if (intent != null) {
            //working on opening fragments
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ProfileFragment()).commit()
        } else {
            //default fragment during app start
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

    }

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navHome ->
                    selectorFragment = HomeFragment()
                R.id.navSearch ->
//                    Toast.makeText(this, "Fragment Search Clicked", Toast.LENGTH_SHORT).show()
                    selectorFragment = SearchFragment()
                R.id.navAddPost ->
                    selectorFragment = AddPostFragment()
                R.id.navProfile ->
                    selectorFragment = ProfileFragment()
            }
            if (selectorFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, selectorFragment!!).commit()
            }
            true
        }

}
