package com.example.posts__comments.addComments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.posts__comments.R
import com.example.posts__comments.addComments.model.CommentItem
import com.example.posts__comments.databinding.CommentRowItemBinding

class CommentAdapter :
    ListAdapter<CommentItem, CommentAdapter.CommentViewHolder>(CommentDiffUtilCallback()) {

    class CommentViewHolder(val binding: CommentRowItemBinding) : ViewHolder(binding.root)
    class CommentDiffUtilCallback : DiffUtil.ItemCallback<CommentItem>() {
        override fun areItemsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CommentItem, newItem: CommentItem): Boolean {
            return oldItem == newItem
        }
    }

    private val iconBackgroundColors = listOf(
        R.color.icon_pink,
        R.color.icon_darkBlue,
        R.color.icon_deepBlue,
        R.color.icon_lightBlue,
        R.color.icon_tan
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = ContextCompat.getSystemService(
            parent.context,
            LayoutInflater::class.java
        ) as LayoutInflater
        val binding = CommentRowItemBinding.inflate(inflater, parent, false)

        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)

        val bgColor = ContextCompat.getColor(
            holder.itemView.context,
            iconBackgroundColors[position % iconBackgroundColors.size]
        )
        val drawable = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_commenter)
        holder.binding.imgComment.setImageDrawable(drawable)
        holder.binding.imgComment.setBackgroundColor(bgColor)
        holder.binding.textCommentName.text = item.name
        holder.binding.textCommentEmail.text = item.email
        holder.binding.textCommentBody.text = item.body
    }
}



