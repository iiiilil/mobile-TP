package com.Refee.RefeeDB

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StatusFragment : Fragment(R.layout.fragment_status) {

    private lateinit var imageUrlTextView: TextView
    private lateinit var imageView: ImageView  // 이미지 URL을 로드할 ImageView
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var humTextView: TextView
    private lateinit var lightTextView: TextView

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Firebase Storage 참조
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    private val handler = Handler(Looper.getMainLooper())  // Handler 초기화
    private lateinit var refreshRunnable: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Storage 초기화
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        // Firebase Realtime Database의 참조 설정
        database = FirebaseDatabase.getInstance().getReference("sensors")
        database2 = FirebaseDatabase.getInstance().getReference("images")

        // TextView 및 ImageView 연결
        humTextView = view.findViewById(R.id.hum)
        lightTextView = view.findViewById(R.id.light)
        imageUrlTextView = view.findViewById(R.id.imageUrlTextView)
        imageView = view.findViewById(R.id.imageView)  // ImageView 연결

        // Firebase 인증 처리
        authenticateUser()
    }

    private fun authenticateUser() {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Authentication successful")
                        initializeDatabaseListeners()
                    } else {
                        Log.w(TAG, "Authentication failed", task.exception)
                    }
                }
        } else {
            initializeDatabaseListeners()
        }
    }

    private fun initializeDatabaseListeners() {
        // 값 변경 리스너 추가 (습도값)
        database.child("humid").child("humid").child("humid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val humValue = dataSnapshot.getValue(Long::class.java)?.toString()
                humTextView.text = "습도: $humValue"
                Log.d(TAG, "Humidity value is: $humValue")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read humidity value.", error.toException())
            }
        })

        // 값 변경 리스너 추가 (광량값)
        database.child("lux").child("lux").child("lux").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lightValue = dataSnapshot.getValue(Long::class.java)?.toString()
                lightTextView.text = "광량: $lightValue"
                Log.d(TAG, "Light value is: $lightValue")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read light value.", error.toException())
            }
        })

        // Firebase Storage에서 이미지 로드
        loadImageFromStorage()

        // 이미지 주기적으로 갱신하는 Runnable 설정
        refreshRunnable = object : Runnable {
            override fun run() {
                // Fragment가 현재 Context에 연결되어 있는지 확인
                context?.let {
                    loadImageFromStorage()  // 이미지를 갱신
                    handler.postDelayed(this, 1000)  // 1초마다 갱신
                }
            }
        }

        // 1초마다 이미지 갱신 시작
        handler.post(refreshRunnable)
    }

    private fun loadImageFromStorage() {
        // Firebase Storage에서 'images' 폴더 내의 'image.jpg' 파일을 참조
        val imageRef = storageRef.child("images/image.jpg")

        // Glide로 이미지 로드
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            // Fragment가 현재 Context에 연결되어 있는지 확인
            context?.let {
                // 이미지 URL을 가져와 Glide로 로드
                Glide.with(it)
                    .load(uri)  // Firebase Storage에서 가져온 이미지 URL
                    .into(imageView)  // ImageView에 로드

                imageUrlTextView.text = "이미지 URL: $uri"
                Log.d(TAG, "Image URL is: $uri")
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Failed to load image from Firebase Storage.", exception)
            Toast.makeText(requireContext(), "이미지 로드 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment가 더 이상 뷰와 연결되지 않을 때 handler를 제거
        handler.removeCallbacks(refreshRunnable)
    }

    companion object {
        private const val TAG = "StatusFragment"
    }
}
