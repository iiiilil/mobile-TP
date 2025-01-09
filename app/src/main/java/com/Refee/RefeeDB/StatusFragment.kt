package com.Refee.RefeeDB

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class StatusFragment : Fragment(R.layout.fragment_status) {

    private lateinit var imageUrlTextView: TextView
    private lateinit var imageView: ImageView  // 이미지 URL을 로드할 ImageView
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var humTextView: TextView
    private lateinit var lightTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Realtime Database의 참조 설정
        database = FirebaseDatabase.getInstance().getReference("sensors")
        database2 = FirebaseDatabase.getInstance().getReference("images")

        // TextView 및 ImageView 연결
        humTextView = view.findViewById(R.id.hum)
        lightTextView = view.findViewById(R.id.light)
        imageUrlTextView = view.findViewById(R.id.imageUrlTextView)
        imageView = view.findViewById(R.id.imageView)  // ImageView 연결

        // 값 변경 리스너 추가 (습도값)
        database.child("humid").child("humid").child("humid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Long 값을 String으로 변환
                val humValue = dataSnapshot.getValue(Long::class.java)?.toString()
                Log.d(TAG, "Humidity value is: $humValue")

                humTextView.text = "습도: $humValue"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read humidity value.", error.toException())
            }
        })

        // 값 변경 리스너 추가 (광량값)
        database.child("lux").child("lux").child("lux").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Long 값을 String으로 변환
                val lightValue = dataSnapshot.getValue(Long::class.java)?.toString()
                Log.d(TAG, "Light value is: $lightValue")

                lightTextView.text = "광량: $lightValue"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read light value.", error.toException())
            }
        })

        // 이미지 URL을 가져오는 리스너 추가
        database2.child("image").child("image_url").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageUrl = dataSnapshot.getValue<String>()
                Log.d(TAG, "Image URL is : $imageUrl")

                // Glide를 사용하여 이미지를 ImageView에 로드
                if (imageUrl != null) {
                    Glide.with(requireContext())
                        .load(imageUrl)  // Firebase에서 가져온 이미지 URL
                        .into(imageView)  // ImageView에 로드
                }

                // URL 텍스트를 표시
                imageUrlTextView.text = "이미지 URL: $imageUrl"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read image URL.", error.toException())
            }
        })
    }

    companion object {
        private const val TAG = "StatusFragment"
    }
}
