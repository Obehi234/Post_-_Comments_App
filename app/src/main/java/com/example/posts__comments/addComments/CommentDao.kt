package com.example.posts__comments.addComments

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.posts__comments.addComments.model.CommentItem

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSingleCommentToDatabase(commentItem : CommentItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCommentListToDatabase(commentItem : List<CommentItem>)

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id DESC")
    suspend fun getCommentFromDatabase(postId: Int): List<CommentItem>

}