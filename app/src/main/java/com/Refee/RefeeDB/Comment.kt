package com.Refee.RefeeDB

data class Comment(
    val postId: String = "",
    val comment: String = "",
    val userId: String = "",
    val timestamp: Long = 0L
)
