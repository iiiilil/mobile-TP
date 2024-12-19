package com.Refee.RefeeDB

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var postDialog: Dialog? = null // popup_post 팝업 참조

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Post 버튼 클릭 리스너 설정
        val postButton: Button = view.findViewById(R.id.btn_post)
        postButton.setOnClickListener {
            showCustomPopup()
        }

        // Edit Profile 버튼 클릭 리스너 설정
        val editProfileButton: Button = view.findViewById(R.id.btn_edit_profile)
        editProfileButton.setOnClickListener {
            loadFragment(EditProfileFragment()) // EditProfileFragment 로드
        }
    }

    private fun showCustomPopup() {
        // Dialog 생성
        postDialog = Dialog(requireContext())
        postDialog?.setContentView(R.layout.popup_post)
        postDialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        // 팝업 크기 설정
        val window = postDialog?.window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt() // 화면 너비의 90%
            layoutParams.height = (resources.displayMetrics.heightPixels * 0.8).toInt() // 화면 높이의 70%
            window.attributes = layoutParams
        }

        // Dialog 내부 요소 설정
        val btnClose = postDialog?.findViewById<ImageButton>(R.id.btn_close_profile)
        val titleInput = postDialog?.findViewById<EditText>(R.id.title_input)
        val bodyInput = postDialog?.findViewById<EditText>(R.id.body_input)
        val btnSave = postDialog?.findViewById<Button>(R.id.btn_save)
        val btnPost = postDialog?.findViewById<Button>(R.id.btn_post_popup)

        // 닫기 버튼 이벤트 처리
        btnClose?.setOnClickListener {
            postDialog?.dismiss()
        }

        // SAVE 버튼 이벤트 처리
        btnSave?.setOnClickListener {
            val title = titleInput?.text.toString().trim()
            val body = bodyInput?.text.toString().trim()

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
                    postDialog?.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // POST 버튼 이벤트 처리
        btnPost?.setOnClickListener {
            val title = titleInput?.text.toString().trim()
            val body = bodyInput?.text.toString().trim()

            if (title.isBlank() || body.isBlank()) {
                Toast.makeText(requireContext(), "제목과 본문을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 확인 팝업 표시
            showConfirmDialog("post") {
                savePostToFirestore(title, body)
            }
        }

        // 팝업 띄우기
        postDialog?.show()
    }


    private fun savePostToFirestore(title: String, body: String) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val post = hashMapOf(
            "title" to title,
            "body" to body,
            "userId" to currentUserId,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("posts")
            .add(post)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "게시 완료!", Toast.LENGTH_SHORT).show()
                postDialog?.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "게시 실패: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showConfirmDialog(action: String, onYes: () -> Unit) {
        val confirmDialog = Dialog(requireContext())
        confirmDialog.setContentView(R.layout.popup_confirmation)
        confirmDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val btnYes = confirmDialog.findViewById<Button>(R.id.btn_yes)
        val btnNo = confirmDialog.findViewById<Button>(R.id.btn_no)
        val messageTextView = confirmDialog.findViewById<TextView>(R.id.confirm_message)
        messageTextView.text = "Are you sure you want to $action?"

        // YES 버튼 이벤트 처리
        btnYes.setOnClickListener {
            onYes() // YES 동작 수행
            postDialog?.dismiss() // popup_post 팝업 닫기
            confirmDialog.dismiss() // 확인 팝업 닫기
        }

        // NO 버튼 이벤트 처리
        btnNo.setOnClickListener {
            postDialog?.dismiss() // popup_post 팝업 닫기
            confirmDialog.dismiss() // 확인 팝업 닫기
        }

        // 확인 팝업 띄우기
        confirmDialog.show()
    }

    // Fragment를 교체하는 메서드
    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null) // 뒤로 가기 지원
        transaction.commit()
    }
}