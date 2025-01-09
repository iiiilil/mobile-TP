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
        private val bodyTextView: TextView = itemView.findViewById(R.id.post_body)  // 본문을 위한 TextView

        fun bind(post: Post) {
            titleTextView.text = post.title // 포스트의 제목을 표시
            bodyTextView.text = post.body // 포스트의 본문을 표시 (여기서 본문을 바인딩)

            itemView.setOnClickListener {
                // 여기서 클릭 시 postId를 포함한 post를 onPostClick으로 전달
                onPostClick(post) // 클릭 시 게시글 자세히 보는 화면으로 이동
            }
        }
    }
}

