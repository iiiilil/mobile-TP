package com.Refee.RefeeDB

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var btnEditProfile: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // findViewById 호출 전에 뷰가 올바르게 초기화되었는지 확인
        val profileImage = view.findViewById<ImageView>(R.id.profile_image)
        // 해당 뷰가 null이 아니어야 합니다
        if (profileImage != null) {
            // profileImage를 사용하는 코드
        } else {
            Log.e("ProfileFragment", "profile_image not found!")
        }

        // 뷰 초기화
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        tvUserName = view.findViewById(R.id.username_text)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)

        // Firestore에서 사용자 데이터 불러오기
        loadUserProfile()

        // Edit Profile 버튼 클릭 리스너 설정
        btnEditProfile.setOnClickListener {
            loadFragment(EditProfileFragment())
        }
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    val imageUrl = document.getString("imageUrl")

                    // 이름 설정
                    tvUserName.text = name ?: "사용자 이름 없음"

                    // 프로필 이미지 설정
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.profile_image_background) // 기본 이미지
                            .into(ivProfilePicture)
                    } else {
                        ivProfilePicture.setImageResource(R.drawable.profile_image_background)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "프로필 불러오기 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Post 버튼 클릭 리스너 설정
        val postButton: Button = requireView().findViewById(R.id.btn_post)
        postButton.setOnClickListener {
            showCustomPopup()
        }

        // Edit Profile 버튼 클릭 리스너 설정
        val editProfileButton: Button? = view?.findViewById(R.id.btn_edit_profile)
        editProfileButton?.setOnClickListener {
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
