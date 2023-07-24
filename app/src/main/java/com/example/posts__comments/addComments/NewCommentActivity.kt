package com.example.posts__comments.addComments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.lifecycleScope
import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.databinding.ActivityNewCommentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCommentActivity : AppCompatActivity() {
    private val commentViewModel by lazy {CommentViewModel()}
    private var _binding: ActivityNewCommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var  add_comment_window_bg : LinearLayout
    private lateinit var  add_comment_text_bg : RelativeLayout
    private lateinit var newCommentTextEdit : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActivityStyle()
        newCommentTextEdit = binding.commentEditText
        val addCommentButton = binding.submitButton
        addCommentButton.setOnClickListener { setUpSubmitButton() }
    }

    private fun setUpSubmitButton() {
           val newCommentText = newCommentTextEdit.text.toString().trim()
            val postId = intent.getIntExtra("postId", -1)
            val newCommentItem = CommentItem(id = 0, postId = postId, name = "", email = "", body = newCommentText)
            Log.d("CHECK_DB", "$newCommentItem - $postId")
            lifecycleScope.launch(Dispatchers.IO) {
                if (newCommentText.isNotEmpty() && postId != -1) {
                    commentViewModel.saveSingleCommentToDatabase(newCommentItem)
                }
            }
            binding.commentEditText.text.clear()
            val resultIntent = Intent()
            resultIntent.putExtra("postId",postId)
            Log.d("CHECK_ID_FINAL", "$postId")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


    private fun setActivityStyle() {
        // Make the background full screen, over status bar
        add_comment_window_bg = binding.addCommentWindowBg
        add_comment_text_bg = binding.addCommentTextBg
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        this.window.statusBarColor = Color.TRANSPARENT
        val winParams = this.window.attributes
        winParams.flags =
            winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        this.window.attributes = winParams

        // Fade animation for the background of Popup Window
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#1B264F"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            add_comment_window_bg.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        add_comment_window_bg.alpha = 0f
        add_comment_window_bg.animate().alpha(1f).setDuration(500)
            .setInterpolator(DecelerateInterpolator()).start()

        // Close window when you tap on the dim background
        add_comment_window_bg.setOnClickListener { onBackPressed() }
        add_comment_text_bg.setOnClickListener { /* Prevent activity from closing when you tap on the popup's window background */ }
    }

    override fun onBackPressed() {
        val alpha = 100
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500
        colorAnimation.addUpdateListener { animator ->
            add_comment_text_bg.setBackgroundColor(
                animator.animatedValue as Int
            )
        }
        add_comment_window_bg.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()

    }

}



