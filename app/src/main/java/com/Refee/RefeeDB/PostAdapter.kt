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
        private val pictureIcon: View = itemView.findViewById(R.id.ic_picture_whether)  // 아이콘을 위한 ImageView

        fun bind(post: Post) {
            titleTextView.text = post.title // 포스트의 제목을 표시
            bodyTextView.text = post.body // 포스트의 본문을 표시

            // 이미지 URL이 존재하면 아이콘 표시
            if (!post.imageUrl.isNullOrEmpty()) {
                pictureIcon.visibility = View.VISIBLE // 이미지가 있으면 아이콘을 보여줌
            } else {
                pictureIcon.visibility = View.GONE // 이미지가 없으면 아이콘을 숨김
            }

            itemView.setOnClickListener {
                // 클릭 시 postId를 포함한 post를 onPostClick으로 전달
                onPostClick(post) // 클릭 시 게시글 자세히 보는 화면으로 이동
            }
        }
    }
}

