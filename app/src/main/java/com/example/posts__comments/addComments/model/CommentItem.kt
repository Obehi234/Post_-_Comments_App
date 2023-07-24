package com.example.posts__comments.addComments.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("comments")
data class CommentItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val body: String,
    val email: String,
    val name: String,
    val postId: Int
)