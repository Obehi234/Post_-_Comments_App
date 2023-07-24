package com.example.posts__comments.addComments.model

import androidx.room.Entity
import androidx.room.Ignore

@Entity("commentList")
class Comment : ArrayList<CommentItem>()