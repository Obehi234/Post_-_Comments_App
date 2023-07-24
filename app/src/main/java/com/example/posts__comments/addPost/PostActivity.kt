package com.example.posts__comments.addPost


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posts__comments.addComments.CommentActivity
import com.example.posts__comments.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity(), PostAdapter.PostRecyclerClickListener {
    private var _binding: ActivityPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PostAdapter
    private lateinit var postViewModel: PostViewModel
    private val postRecyclerView by lazy { binding.quoteRecycler }
    private val progressBar by lazy { binding.postProgressBar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.setHasFixedSize(true)
        adapter = PostAdapter()
        adapter.setItemClickListener(this)
        postRecyclerView.adapter = adapter
        postViewModel.fetchPosts()
        showProgressBar()
        postViewModel.post.observe(this, Observer {
            hideProgressBar()
            adapter.submitList(it)
        })
    }

    override fun onPostItemClick(adapterPosition: Int) {
        val clickedPost = adapter.currentList[adapterPosition]
        val postId = clickedPost.id
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        startActivity(intent)
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}