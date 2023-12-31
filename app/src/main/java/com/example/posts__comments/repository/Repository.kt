package com.example.posts__comments.repository

import android.content.Context
import com.example.posts__comments.addComments.CommentDao
import com.example.posts__comments.addComments.CommentDatabase
import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.addPost.model.PostItem
import com.example.posts__comments.api.ApiService
import com.example.posts__comments.api.RetrofitInstance


object Repository {
    private val apiService: ApiService =
        RetrofitInstance.getInstance().create(ApiService::class.java)
    private lateinit var commentDatabase: CommentDatabase
    private lateinit var getCommentDao: CommentDao


    suspend fun getPosts(): List<PostItem> {
        return apiService.getPosts()
    }

    fun initializeDatabase(context: Context) {
        commentDatabase = CommentDatabase.getDatabase(context)
        getCommentDao = commentDatabase.commentDao()
    }

    suspend fun getComments(postId: Int): List<CommentItem> {
        val remoteResponse = apiService.getComments(postId)
        getCommentDao.addCommentListToDatabase(remoteResponse)
        return getCommentDao.getCommentFromDatabase(postId)
    }

    suspend fun saveSingleCommentToDatabase(comment: CommentItem) {
        getCommentDao.addSingleCommentToDatabase(comment)
    }
}