package com.example.posts__comments.addComments

import android.content.Context
import android.util.Log
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
    private val commentLiveData: LiveData<List<CommentItem>>get() = _commentLiveData

    fun initializeDatabase(context: Context) {
        repository.initializeDatabase(context)
    }

    suspend fun getCommentsForSpecificPost(postId : Int) : LiveData<List<CommentItem>> {
            viewModelScope.launch(Dispatchers.IO){
                try{
                    val comments = repository.getComments(postId)
                    //This is a list from database.
                    withContext(Dispatchers.Main) {
                        _commentLiveData.postValue(comments)
                        //We then assign this retrieved list to live data so we can monitor it for changes
                        Log.d("CHECK_LIST_SUCCESS", "${comments.size}- $postId")
                    }

                }catch (e: Exception) {
                    Log.d("CHECK_LIST_FAILED", "${e.message}")
                }
            }
        return commentLiveData
    }

    suspend fun saveSingleCommentToDatabase(comment: CommentItem) {
        viewModelScope.launch {
            try{
                repository.saveSingleCommentToDatabase(comment)
                Log.d("CHECK_SAVE_SUCCESS", "$comment")
                //Defined in Dao to add single manual comment to database
            }catch (e: Exception) {
                Log.d("CHECK_SAVE_FAILED", "${e.message}")
            }
        }
    }
}