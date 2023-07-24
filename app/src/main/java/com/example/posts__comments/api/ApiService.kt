package com.example.posts__comments.api

import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.addPost.model.PostItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<PostItem>

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Int): List<CommentItem>

}