package com.Refee.RefeeDB

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Post 버튼 클릭 리스너 설정
        val postButton: Button = view.findViewById(R.id.btn_post)
        postButton.setOnClickListener {
            showCustomPopup()
        }
    }

    private fun showCustomPopup() {
        // Dialog 생성
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_post)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        // 팝업 크기 설정
        val window = dialog.window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt() // 화면 너비의 90%
            layoutParams.height = (resources.displayMetrics.heightPixels * 0.8).toInt() // 화면 높이의 70%
            window.attributes = layoutParams
        }

        // Dialog 내부 요소 설정
        val btnClose = dialog.findViewById<ImageButton>(R.id.btn_close_profile)
        val titleInput = dialog.findViewById<EditText>(R.id.title_input)
        val bodyInput = dialog.findViewById<EditText>(R.id.body_input)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnPost = dialog.findViewById<Button>(R.id.btn_post_popup)

        // 닫기 버튼 이벤트 처리
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        // SAVE 버튼 이벤트 처리
        btnSave.setOnClickListener {
            val title = titleInput.text.toString()
            val body = bodyInput.text.toString()

            if (title.isBlank() || body.isBlank()) {
                Toast.makeText(requireContext(), "제목과 본문을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val draft = hashMapOf(
                "title" to title,
                "body" to body,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("drafts")
                .add(draft)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "임시 저장 완료", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // POST 버튼 이벤트 처리
        btnPost.setOnClickListener {
            val title = titleInput.text.toString()
            val body = bodyInput.text.toString()

            if (title.isBlank() || body.isBlank()) {
                Toast.makeText(requireContext(), "제목과 본문을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Post 데이터
            val post = hashMapOf(
                "title" to title,
                "body" to body,
                "userId" to "currentUserId", // 실제 사용자 ID로 변경
                "timestamp" to System.currentTimeMillis()
            )

            // Firestore에 posts 컬렉션에 저장
            firestore.collection("posts")  // "refeedb"의 posts 컬렉션
                .add(post)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "게시 완료!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "게시 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // 팝업 띄우기
        dialog.show()
    }
}
