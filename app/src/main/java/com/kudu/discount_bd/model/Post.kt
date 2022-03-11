package com.kudu.discount_bd.model

data class Post(
    var description: String? = null,
    var creationTime: Long = 0,
    var imageUrl: String? = null,
    var publisher: Seller? = null
)
