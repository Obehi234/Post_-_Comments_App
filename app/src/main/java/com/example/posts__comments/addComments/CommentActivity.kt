package com.example.posts__comments.addComments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.databinding.ActivityCommentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentActivity : AppCompatActivity() {
    private lateinit var commentViewModel: CommentViewModel
    private val commentAdapter by lazy {CommentAdapter()}
    private var _binding: ActivityCommentBinding? = null
    private val binding get() = _binding!!
    private val commentRecyclerView by lazy { binding.commentsRecycler}
    private val progressBar by lazy { binding.progressHome }
    private var postId: Int = -1
    private lateinit var newCommentResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Comments"
        commentViewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        commentViewModel.initializeDatabase(this)

        setUpRecyclerView()
        setUpFAButton()
        setUpNewActivityLauncher()

    }

    private fun setUpNewActivityLauncher() {
        newCommentResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                postId = data?.getIntExtra("postId", -1) ?: -1
                val newCommentText = data?.getStringExtra("new_comment_text")

                if(newCommentText != null && postId != -1) {
                    val comments = commentAdapter.currentList.toMutableList()
                    val newCommentItem = CommentItem(id = 0, postId = postId,name = "", email = "", body = newCommentText )
                    comments.add(newCommentItem)

                    commentAdapter.submitList(comments)
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        commentRecyclerView.adapter = commentAdapter
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.setHasFixedSize(true)
        postId = intent.getIntExtra("postId",  -1)
        if(postId != -1) {
            showProgressBar()
            lifecycleScope.launch(Dispatchers.Main) {
               commentViewModel.getCommentsForSpecificPost(postId).observe(this@CommentActivity, Observer {comment ->
                   hideProgressBar()
                   Log.d("CHECK_COMMENT", "$comment")
                   if (comment != null) {
                       commentAdapter.submitList(comment)
                   }
               })
             }
        }
    }

    private fun setUpFAButton() {
        setUpNewActivityLauncher()
        binding.fabAddComment.setOnClickListener {
            Log.d("CHECK_FAB", "$postId")
            val intent = Intent(this, NewCommentActivity::class.java)
            intent.putExtra("postId", postId)
            newCommentResultLauncher.launch(intent)
        }
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            postId = data?.getIntExtra("postId", -1) ?: -1
            if(postId != -1) {
                showProgressBar()
                lifecycleScope.launch (Dispatchers.Main){
                    commentViewModel.getCommentsForSpecificPost(postId).observe(this@CommentActivity, Observer { comment ->
                        hideProgressBar()
                        if(comment != null) {
                            Log.d("CHECK_Res_COMMENT", "$comment - ${comment.size}")
                            commentAdapter.submitList(comment)
                        }
                    })
                }
            }
        }
    }


}