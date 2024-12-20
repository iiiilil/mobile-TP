package com.Refee.RefeeDB

data class Post(
    val id : String = "",
    val title: String = "",
    val body: String = "",
    val userId: String = "",
    val timestamp: Long = 0L
)