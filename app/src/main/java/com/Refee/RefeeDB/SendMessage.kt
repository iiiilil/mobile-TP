package com.Refee.RefeeDB

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SendMessage : AppCompatActivity() {

    private lateinit var postId: String
    private lateinit var postTitle: String
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        // Intent로 받은 데이터
        postId = intent.getStringExtra("postId") ?: ""
        postTitle = intent.getStringExtra("postTitle") ?: ""

        messageInput = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.btn_send)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isBlank()) {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firestore에 메시지 저장
            val messageData = hashMapOf(
                "postId" to postId,
                "message" to message,
                "senderId" to FirebaseAuth.getInstance().currentUser?.uid,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("messages")
                .add(messageData)
                .addOnSuccessListener {
                    Toast.makeText(this, "메시지 전송 완료", Toast.LENGTH_SHORT).show()
                    finish() // 액티비티 종료
                }
                .addOnFailureListener {
                    Toast.makeText(this, "메시지 전송 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
