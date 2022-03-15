package com.kudu.discount_bd.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kudu.discount_bd.R
import com.kudu.discount_bd.adapter.SellerAdapter
import com.kudu.discount_bd.databinding.FragmentSearchBinding
import com.kudu.discount_bd.model.Seller
import java.util.*

class SearchFragment : Fragment() {

    companion object {
        lateinit var binding: FragmentSearchBinding

        lateinit var sellerList: MutableList<Seller>
        lateinit var tempSellerList: MutableList<Seller>
    }

    private lateinit var sellerAdapter: SellerAdapter
    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view)

        //datasource
        sellerList = mutableListOf()
        tempSellerList = mutableListOf()

        //adapter
//        sellerAdapter = SellerAdapter(requireContext(), sellerList)
        sellerAdapter = SellerAdapter(requireContext(), tempSellerList)
        binding.sellerRV.setHasFixedSize(true)
        binding.sellerRV.setItemViewCacheSize(20)
        binding.sellerRV.adapter = sellerAdapter
        binding.sellerRV.layoutManager = LinearLayoutManager(context)

        sellerAdapter.setOnItemClickListener(object: SellerAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                Toast.makeText(context, "$position clicked", Toast.LENGTH_SHORT).show()
            }

        })

        readUsers()
        searchUsers()

        return view
    }

    private fun searchUsers() {
        binding.sellerRV.visibility = View.VISIBLE
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempSellerList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    sellerList.forEach{
                        if (it.userName!!.lowercase(Locale.getDefault()).contains(searchText)){
                            tempSellerList.add(it)
                        }
                    }
                    sellerAdapter.notifyDataSetChanged()
                }else{
                    tempSellerList.clear()
                    tempSellerList.addAll(sellerList)
                    sellerAdapter.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    private fun readUsers() {
//        binding.sellerRV.visibility = View.INVISIBLE
        firestoreDb = FirebaseFirestore.getInstance()
        val sellersReference = firestoreDb.collection("sellers")
            .limit(20)
//            .orderBy("creationTime", Query.Direction.DESCENDING)

        sellersReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(ContentValues.TAG, "Exception when querying sellers", exception)
                return@addSnapshotListener
            }

            val sellersList = snapshot.toObjects(Seller::class.java)
            sellerList.clear()
            sellerList.addAll(sellersList)
            sellerAdapter.notifyDataSetChanged()
            for (seller in sellersList) {
                Log.d(ContentValues.TAG, "Seller: $sellersList")
            }

            tempSellerList.addAll(sellersList)
        }


    }



}