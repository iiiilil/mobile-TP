package com.Refee.RefeeDB

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var postDialog: Dialog? = null // popup_post 팝업 참조

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchProfileData()

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
        val viewStoriesButton: Button = view.findViewById(R.id.btn_view_stories)
        viewStoriesButton.setOnClickListener {
            loadFragment(MyItemFragment()) // EditProfileFragment 로드
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

        // 취소 버튼 이벤트 처리
        btnSave?.setOnClickListener {
            // 데이터 입력 없이 그냥 팝업 닫기
            postDialog?.dismiss()  // 팝업 닫기

            // 취소 처리에 대한 메시지를 출력하려면 (선택 사항)ㅓ
            Toast.makeText(requireContext(), "작성이 취소되었습니다.", Toast.LENGTH_SHORT).show()
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

    private fun fetchProfileData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load profile: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val name = snapshot.getString("name") ?: "Unknown"
                    val imageUrl = snapshot.getString("imageUrl")

                    // UI 업데이트
                    view?.findViewById<TextView>(R.id.username_text)?.text = name
                    val profileImage = view?.findViewById<ImageView>(R.id.profile_image)
                    if (imageUrl != null) {
                        // Glide 또는 Picasso로 이미지 로드
                        if (profileImage != null) {
                            Glide.with(this).load(imageUrl).into(profileImage)
                        }
                    }
                }
            }
    }
}