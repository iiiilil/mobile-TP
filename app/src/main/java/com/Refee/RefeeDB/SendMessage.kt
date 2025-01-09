package com.Refee.RefeeDB

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SendMessage : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var postTitle: String
    private lateinit var postBody: String  // 본문 내용 추가
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var textPostTitle: TextView  // 제목 표시용 TextView 추가
    private lateinit var textPostBody: TextView   // 본문 표시용 TextView 추가
    private lateinit var postImageView: ImageView // 이미지 표시용 ImageView 추가
    private lateinit var recyclerView: RecyclerView

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val comments = mutableListOf<Comment>()
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        // Toolbar 설정 (뒤로가기 버튼과 "알림" 텍스트)
        val toolbar = findViewById<Toolbar>(R.id.post_toolbar)
        setSupportActionBar(toolbar)

        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 프로젝트 이름과 ic_refee 아이콘을 제거
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(null)  // 아이콘 제거

        // "알림" 텍스트 설정
        toolbar.title = "게시글"

        // Intent로 받은 데이터
        postId = intent.getStringExtra("postId") ?: ""
        if (postId.isEmpty()) {
            Log.e("SendMessage", "postId가 전달되지 않았습니다.")
        }
        postTitle = intent.getStringExtra("postTitle") ?: ""
        postBody = intent.getStringExtra("postBody") ?: "" // 본문 내용 추가

        // UI 초기화
        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.btn_send)
        textPostTitle = findViewById(R.id.text_post_title)  // 제목 표시용 TextView 초기화
        textPostBody = findViewById(R.id.text_post_body)    // 본문 표시용 TextView 초기화
        postImageView = findViewById(R.id.post_image)       // 이미지 표시용 ImageView 초기화
        recyclerView = findViewById(R.id.recycler_view_comments)

        // 제목과 본문 표시
        textPostTitle.text = postTitle
        textPostBody.text = postBody

        // 댓글 RecyclerView 설정
        commentAdapter = CommentAdapter(comments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = commentAdapter

        // 게시글 데이터 가져오기
        fetchPostData()

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isBlank()) {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 댓글 저장
            postComment(message)
        }
    }

    // 뒤로가기 버튼 클릭 시 MainActivity로 이동
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭 시 MainActivity로 이동
                val intent = Intent(this, MainScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // MainActivity로 돌아가기
                startActivity(intent)
                finish() // 현재 Activity 종료
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 게시글 데이터 가져오기
    private fun fetchPostData() {
        firestore.collection("posts").document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val post = document.toObject(Post::class.java)
                    // 제목과 본문 설정
                    post?.let {
                        textPostTitle.text = it.title
                        textPostBody.text = it.body

                        // 이미지 URL이 없으면 ImageView 숨기기
                        if (it.imageUrl.isNullOrEmpty()) {
                            postImageView.visibility = ImageView.GONE
                        } else {
                            postImageView.visibility = ImageView.VISIBLE
                            // 이미지 URL이 있으면 Glide로 이미지 로드
                            Glide.with(this).load(it.imageUrl).into(postImageView)
                        }
                    }
                } else {
                    Log.e("SendMessage", "게시글을 찾을 수 없습니다.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SendMessage", "게시글 불러오기 실패: ${exception.message}")
            }
    }

    // 댓글을 Firestore의 새 컬렉션 `comments`에 저장하는 메서드
    private fun postComment(comment: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val commentData = hashMapOf(
            "postId" to postId,  // 게시글 고유 ID
            "comment" to comment,  // 댓글 내용
            "userId" to currentUserId,  // 댓글 작성자 ID
            "timestamp" to System.currentTimeMillis()  // 댓글 작성 시간
        )

        // 댓글을 `comments` 컬렉션에 저장
        firestore.collection("comments")  // 새 컬렉션 `comments`
            .add(commentData)  // 댓글 데이터를 추가
            .addOnSuccessListener {
                Toast.makeText(this, "댓글이 게시되었습니다.", Toast.LENGTH_SHORT).show()
                messageInput.text.clear()  // 댓글 입력창 초기화

                // 댓글이 성공적으로 추가된 후, 댓글 리스트를 다시 불러와서 UI 업데이트
                fetchComments()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "댓글 게시 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Firestore에서 해당 게시물의 댓글을 가져오는 메서드
    private fun fetchComments() {
        // Firestore에서 해당 게시물의 댓글만 가져오기
        firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.e("SendMessage", "해당 게시물에 댓글이 없습니다.")
                } else {
                    comments.clear()
                    for (document in result) {
                        val comment = document.toObject(Comment::class.java)
                        comments.add(comment)
                    }
                    commentAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.e("SendMessage", "댓글 불러오기 실패: ${e.message}")
            }
    }
}
