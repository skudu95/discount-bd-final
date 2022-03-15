package com.kudu.discount_bd.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kudu.discount_bd.databinding.SellerlistCardviewLayoutBinding
import com.kudu.discount_bd.model.Seller

class SellerAdapter(val context: Context,
    private var sellerList: List<Seller>) :
    RecyclerView.Adapter<SellerAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class ViewHolder(binding: SellerlistCardviewLayoutBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val userName = binding.userNameSL
        val sShopName = binding.shopNameSL
        val sProfileImg = binding.profileImgSL

        init {
//            itemView.setOnClickListener{
            root.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SellerlistCardviewLayoutBinding.inflate(LayoutInflater.from(context), parent, false), mListener)
//        return ViewHolder(SellerlistCardviewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seller = sellerList[position]

        holder.userName.text = seller.userName
        holder.sShopName.text = seller.shopName

    }

    override fun getItemCount(): Int {
        return sellerList.size
    }


}