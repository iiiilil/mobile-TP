package com.Refee.RefeeDB

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private val posts: List<Post>,
    private val onPostClick: (Post) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.post_title)

        fun bind(post: Post) {
            titleTextView.text = post.title // 포스트의 제목을 표시
            itemView.setOnClickListener {
                onPostClick(post) // 클릭 시 메시지 보내는 화면으로 이동
            }
        }
    }
}
