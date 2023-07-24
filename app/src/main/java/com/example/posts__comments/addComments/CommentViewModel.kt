package com.example.posts__comments.addComments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommentViewModel : ViewModel() {
    private val repository: Repository = Repository
    private val _commentLiveData = MutableLiveData<List<CommentItem>>()
    private val commentLiveData: LiveData<List<CommentItem>> get() = _commentLiveData

    fun initializeDatabase(context: Context) {
        repository.initializeDatabase(context)
    }

    suspend fun getCommentsForSpecificPost(postId: Int): LiveData<List<CommentItem>> {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val comments = repository.getComments(postId)
                withContext(Dispatchers.Main) {
                    _commentLiveData.postValue(comments)
                }

            } catch (_: Exception) {

            }
        }
        return commentLiveData
    }

    suspend fun saveSingleCommentToDatabase(comment: CommentItem) {
        viewModelScope.launch {
            try {
                repository.saveSingleCommentToDatabase(comment)
            } catch (_: Exception) {

            }
        }
    }
}