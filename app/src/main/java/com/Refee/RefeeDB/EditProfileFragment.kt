package com.Refee.RefeeDB

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private lateinit var ivProfilePicture: ImageView
    private lateinit var etName: EditText
    private lateinit var btnChangePicture: Button
    private lateinit var btnSave: Button

    private var selectedImageUri: Uri? = null

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    // 사진 선택 콜백
    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivProfilePicture.setImageURI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 초기화
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture)
        etName = view.findViewById(R.id.etName)
        btnChangePicture = view.findViewById(R.id.btnChangePicture)
        btnSave = view.findViewById(R.id.btnSave)

        // 사진 변경 버튼 클릭 리스너
        btnChangePicture.setOnClickListener {
            selectImageLauncher.launch("image/*") // 이미지 파일 선택
        }

        // 저장 버튼 클릭 리스너
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                saveProfile(name, selectedImageUri)
            }
        }
    }

    private fun saveProfile(name: String, imageUri: Uri?) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "You need to be logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri != null) {
            // 이미지가 선택된 경우 Firebase Storage에 업로드
            val imageRef = storage.reference.child("profile_pictures/$userId/${UUID.randomUUID()}.jpg")
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        saveToFirestore(userId, name, uri.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // 이미지가 선택되지 않은 경우 이름만 저장
            saveToFirestore(userId, name, null)
        }
    }

    private fun saveToFirestore(userId: String, name: String, imageUrl: String?) {
        val userProfile = hashMapOf(
            "name" to name,
            "imageUrl" to imageUrl,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("users").document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to save profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
