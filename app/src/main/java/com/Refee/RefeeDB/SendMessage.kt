package com.Refee.RefeeDB

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var recyclerView: RecyclerView

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val comments = mutableListOf<Comment>()
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

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
        recyclerView = findViewById(R.id.recycler_view_comments)

        // 제목과 본문 표시
        textPostTitle.text = postTitle
        textPostBody.text = postBody

        // 댓글 RecyclerView 설정
        commentAdapter = CommentAdapter(comments)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = commentAdapter

        // 댓글 가져오기
        fetchComments()

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
            //.orderBy("timestamp")
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
