package com.example.posts__comments.addPost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts__comments.addPost.model.PostItem
import com.example.posts__comments.repository.Repository
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val repository: Repository = Repository
    private var _post = MutableLiveData<List<PostItem>>()
    private val TAG = "CHECK_RESPONSE_POST"

    val post : LiveData<List<PostItem>>
    get() = _post

    init{
        fetchPostFromApi()
    }

    private fun fetchPostFromApi() {
        viewModelScope.launch {
            try{
                val post = repository.getPosts()
                _post.value = post
                Log.d("TAG", "${post.size}")
            } catch (e: Exception) {
                Log.d("TAG", "${e.message}")
            }
        }
    }
    fun fetchPosts() {
        fetchPostFromApi()
    }

}