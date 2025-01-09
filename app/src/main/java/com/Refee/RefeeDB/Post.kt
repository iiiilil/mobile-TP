package com.Refee.RefeeDB

data class Post(
    val id : String = "",
    val postId: String = "",   // 게시글의 고유 ID (사용자가 정의하는 고유 ID)
    val title: String = "",
    val body: String = "",
    val userId: String = "",
    val timestamp: Long = 0L
)